package pers.qjw.seckill.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.jasypt.util.text.BasicTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pers.qjw.seckill.config.Constants;
import pers.qjw.seckill.domain.Goods;
import pers.qjw.seckill.domain.Order;
import pers.qjw.seckill.exception.*;
import pers.qjw.seckill.service.GoodsService;
import pers.qjw.seckill.service.OrderService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class OrderServiceImpl implements OrderService {

    private BasicTextEncryptor basicTextEncryptor;

    private GoodsService goodsService;

    private StringRedisTemplate stringRedisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public OrderServiceImpl() {
    }

    @Autowired
    public OrderServiceImpl(BasicTextEncryptor basicTextEncryptor, GoodsService goodsService, StringRedisTemplate stringRedisTemplate) {
        this.basicTextEncryptor = basicTextEncryptor;
        this.goodsService = goodsService;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    // 检查 randomText 是不是由服务端生成的
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

    private int inventoryReduction(String key) {
        String script =
                "    local inventory = tonumber(redis.call('get', KEYS[1]));" +
                        "    if (inventory > 0) then" +
                        "        return redis.call('incrby', KEYS[1], -1);" +
                        "    end;" +
                        "    return 0;";
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

    private synchronized void createCache(int goodsId, String key) {
        String inventory = stringRedisTemplate.opsForValue().get(key);
        if (Objects.isNull(inventory)) {
            Goods goods = goodsService.getGoods(String.valueOf(goodsId));
            String value = String.valueOf(goods.getNumber());
            stringRedisTemplate.opsForValue().set(key, value);
        }
    }

    private void maintenanceCache(int goodsId, int result) {
        // 维护 hotCommodity 缓存
        List<Goods> hotCommodity = goodsService.getHotCommodity();
        goodsService.maintenanceHotCommodityCache(hotCommodity, goodsId, result);
        // 维护 getGoods 缓存
        Goods goods = goodsService.getGoods(String.valueOf(goodsId));
        goodsService.maintenanceGoodsCache(goods, goodsId, result);
        if (result == 0) {
            stringRedisTemplate.delete(Constants.INVENTORY + goodsId);
        }
    }

    private void WriteToCache(Order order) {
        String json = JSONObject.toJSONString(order);
        stringRedisTemplate.opsForSet().add(Constants.ORDER_SET,json);
    }

    private void maintenanceData(int goodsId, int inventory) {
        int flag = goodsService.setInventory(goodsId, inventory);
        if (flag != 1) {
            logger.error("更新商品库存信息失败" + goodsId + "号商品已售空。");
        }
    }

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

        // 判断用户有没有买过该商品


        // 减库存
        int result = inventoryReduction(key);
        // 维护缓存
        maintenanceCache(goodsId, result);
        if (result == 0) {
            // 不能创建订单，商品已经卖完了
            // 维护数据库中的数据
            maintenanceData(goodsId, result);
            throw new ConditionNotMetException("商品已售空", HttpStatus.BAD_REQUEST);
        } else {
            // 可以创建订单，商品数量成功-1
            Order order = new Order();
            order.setUserId(userId);
            order.setGoodsId(goodsId);
            order.setState(Order.NON_PAYMENT);
            order.setCreateTime(new Timestamp(new Date().getTime()));
            WriteToCache(order);
        }
    }
}
