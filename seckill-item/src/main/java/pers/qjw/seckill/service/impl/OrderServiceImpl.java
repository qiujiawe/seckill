package pers.qjw.seckill.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.jasypt.util.text.BasicTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pers.qjw.seckill.config.Constants;
import pers.qjw.seckill.dao.OrderDao;
import pers.qjw.seckill.domain.Goods;
import pers.qjw.seckill.domain.Order;
import pers.qjw.seckill.domain.OrderVO;
import pers.qjw.seckill.exception.*;
import pers.qjw.seckill.service.GoodsService;
import pers.qjw.seckill.service.OrderService;
import pers.qjw.seckill.timing.OrderFailureManagement;
import pers.qjw.seckill.timing.PurchaseManagement;
import pers.qjw.seckill.util.GenerateId;

import java.sql.Timestamp;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final PurchaseManagement purchaseManagement;

    private final BasicTextEncryptor basicTextEncryptor;

    private final GoodsService goodsService;

    private final StringRedisTemplate stringRedisTemplate;

    private final OrderDao orderDao;

    private final GenerateId generateId;

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public OrderServiceImpl(PurchaseManagement purchaseManagement, BasicTextEncryptor basicTextEncryptor, GoodsService goodsService, StringRedisTemplate stringRedisTemplate, OrderDao orderDao, GenerateId generateId) {
        this.purchaseManagement = purchaseManagement;
        this.basicTextEncryptor = basicTextEncryptor;
        this.goodsService = goodsService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.orderDao = orderDao;
        this.generateId = generateId;
    }

    // 检查 randomText 是不是由服务端生成的,如果是则解析成商品id
    private int checkRandomText(String randomText) {
        int id;
        try {
            String goodsId = basicTextEncryptor.decrypt(randomText);
            id = Integer.parseInt(goodsId);
        } catch (Exception e) {
            throw new ClientDataErrorException("链接不是由服务端生成的", HttpStatus.BAD_REQUEST);
        }
        return id;
    }

    // 利用redis的原子性加实现 减少库存(加-1)
    private int inventoryReduction(String key) {
        String script =
                "    local inventory = tonumber(redis.call('get', KEYS[1]));" +
                        "    if (inventory >= 0) then" +
                        "        return redis.call('incrby', KEYS[1], -1);" +
                        "    end;" +
                        "    return -1;";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        List<String> keys = new ArrayList<>();
        keys.add(key);
        Long flag;
        try {
            flag = stringRedisTemplate.execute(redisScript, keys);
        } catch (Exception e) {
            throw new LuaPerformFailureException("创建订单失败", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        long result;
        if (Objects.isNull(flag)) {
            throw new LuaPerformFailureException("创建订单失败", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            result = flag;
        }
        return (int) result;
    }

    // 初始化redis中的 库存 字段，用于实现 原子性库存递减
    private synchronized void createCache(int goodsId, String key) {
        String inventory = stringRedisTemplate.opsForValue().get(key);
        if (Objects.isNull(inventory)) {
            Goods goods = goodsService.getGoods(String.valueOf(goodsId));
            String value = String.valueOf(goods.getNumber());
            stringRedisTemplate.opsForValue().set(key, value);
        }
    }

    // 修改商品库存字段后 需要维护其他包含商品库存的字段
    private void maintenanceCache(int goodsId, int result) {
        if (result < 0) {
            result = 0;
        }
        // 维护 hotCommodity 缓存
        List<Goods> hotCommodity = goodsService.getHotCommodity();
        goodsService.maintenanceHotCommodityCache(hotCommodity, goodsId, result);
        // 维护 getGoods 缓存
        Goods goods = goodsService.getGoods(String.valueOf(goodsId));
        goodsService.maintenanceGoodsCache(goods, goodsId, result);
        if (result == 0) {
            if (!Objects.isNull(stringRedisTemplate.opsForValue().get(Constants.INVENTORY + goodsId)))
                stringRedisTemplate.delete(Constants.INVENTORY + goodsId);
        }
    }

    // 将创建的订单存入redis，之后会有线程将redis中的订单信息分批写入数据库
    private void WriteToCache(Order order) {
        String json = JSONObject.toJSONString(order);
        stringRedisTemplate.opsForSet().add(Constants.ORDER_SET, json);
    }

    // 商品售空后要将商品售空的消息写入数据库(商品在没售空的时候 数据库中的商品库存 与 其他地方的商品库存 是不同步的)
    private void maintenanceData(int goodsId) {
        int flag = goodsService.setInventory(goodsId, 0);
        if (flag != 1) {
            logger.error("更新商品库存信息失败" + goodsId + "号商品已售空。");
        }
    }

    // 创建订单
    @Override
    public void createOrder(String randomText, Integer userId) {
        // 解析url
        int goodsId = checkRandomText(randomText);
        // 判断缓存中是否有 商品库存字段
        String key = Constants.INVENTORY + goodsId;
        String inventory = stringRedisTemplate.opsForValue().get(key);
        if (Objects.isNull(inventory)) {
            // 没有则创建
            createCache(goodsId, key);
        }
        // 判断用户5分钟前有没有买过该商品
        purchaseManagement.bought(userId, goodsId);
        // 减库存
        int result = inventoryReduction(key);
        // 维护缓存
        maintenanceCache(goodsId, result);
        if (result < 0) {
            // 不能创建订单，商品已经卖完了
            // 维护数据库中的数据
            maintenanceData(goodsId);
            throw new ConditionNotMetException("商品已售空", HttpStatus.BAD_REQUEST);
        } else {
            // 可以创建订单，商品数量成功-1
            Order order = new Order();
            order.setId(generateId.generateId());
            order.setUserId(userId);
            order.setGoodsId(goodsId);
            order.setState(Order.NON_PAYMENT);
            order.setCreateTime(new Timestamp(new Date().getTime()));
            // 添加购买记录
            purchaseManagement.addRecords(userId, goodsId);
            // 将订单信息写入缓存
            WriteToCache(order);
            // 开启一个新的线程，用于监听当前订单是否在有效时间内付款
            OrderFailureManagement orderFailureManagement = new OrderFailureManagement(order, stringRedisTemplate, orderDao, goodsService);
            orderFailureManagement.start();
        }
    }

    @Override
    public List<OrderVO> listOrders(Integer id) {
        List<OrderVO> result = new ArrayList<>();
        Set<String> set = stringRedisTemplate.opsForSet().members(Constants.ORDER_SET);
        List<Order> orderList = orderDao.listOrders(id);
        if (!Objects.isNull(set)) {
            for (String json : set) {
                Order order = JSONObject.parseObject(json, Order.class);
                if (Objects.equals(order.getUserId(), id)) {
                    Goods goods = goodsService.getGoods(String.valueOf(order.getGoodsId()));
                    result.add(new OrderVO(order, goods.getName()));
                }
            }
        }
        for (Order temp : orderList) {
            Goods goods = goodsService.getGoods(String.valueOf(temp.getGoodsId()));
            result.add(new OrderVO(temp, goods.getName()));
        }
        return result;

    }

    @Override
    public void pay(String orderId) {
        int id;
        try {
            id = Integer.parseInt(orderId);
        } catch (Exception e) {
            throw new ClientDataErrorException("错误的订单编号", HttpStatus.BAD_REQUEST);
        }
        Set<String> set = stringRedisTemplate.opsForSet().members(Constants.ORDER_SET);
        if (!Objects.isNull(set)) {
            for (String json : set) {
                Order order = JSONObject.parseObject(json, Order.class);
                if (Objects.equals(order.getId(), id)) {
                    if (!Objects.equals(order.getState(), Order.LOSE_EFFICACY)) {
                        order.setState(Order.HAVE_PAID);
                        stringRedisTemplate.opsForSet().remove(Constants.ORDER_SET, json);
                        stringRedisTemplate.opsForSet().add(Constants.ORDER_SET,JSONObject.toJSONString(order));
                    } else {
                        throw new ClientDataErrorException("订单已超时，不能支付", HttpStatus.FORBIDDEN);
                    }
                }
            }
        }
        Order order = orderDao.getOrder(id);
        if (!Objects.isNull(order)) {
            if (!Objects.equals(order.getState(), Order.LOSE_EFFICACY)) {
                orderDao.updateOrder(id, Order.HAVE_PAID);
            } else {
                throw new ClientDataErrorException("订单已超时，不能支付", HttpStatus.FORBIDDEN);
            }
        }
    }
}
