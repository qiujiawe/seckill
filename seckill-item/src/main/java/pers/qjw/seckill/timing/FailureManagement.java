package pers.qjw.seckill.timing;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import pers.qjw.seckill.config.Constants;
import pers.qjw.seckill.dao.OrderDao;
import pers.qjw.seckill.domain.Goods;
import pers.qjw.seckill.domain.Order;
import pers.qjw.seckill.exception.GlobalExceptionHandler;
import pers.qjw.seckill.service.GoodsService;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class FailureManagement extends Thread {

    private final Order order;

    private final StringRedisTemplate stringRedisTemplate;

    private final OrderDao orderDao;

    private final GoodsService goodsService;

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public FailureManagement(Order order, StringRedisTemplate stringRedisTemplate, OrderDao orderDao, GoodsService goodsService) {
        this.orderDao = orderDao;
        this.stringRedisTemplate = stringRedisTemplate;
        this.order = order;
        this.goodsService = goodsService;
    }

    private int inventoryIncrease(String key) {
        String script = "return redis.call('incrby', KEYS[1], 1);";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        List<String> keys = new ArrayList<>();
        keys.add(key);
        Long result = null;
        try {
            result = stringRedisTemplate.execute(redisScript, keys);
        } catch (Exception e) {
            logger.error("加库存失败，库存key:" + key, e);
        }
        if (!Objects.isNull(result)) {
            return (int) (long) result;
        } else {
            return -1;
        }
    }


    private void pause() {
        // 计算暂停时间
        long failureTime = order.getCreateTime().getTime() + 300000;
        long left = failureTime - new Date().getTime();
        try {
            TimeUnit.MILLISECONDS.sleep(left);
        } catch (InterruptedException e) {
            logger.error("管理订单状态的类出现问题", e);
        }
    }

    private void maintenanceCache(String member) {
        order.setState(Order.LOSE_EFFICACY);
        String json = JSONObject.toJSONString(order);
        stringRedisTemplate.opsForSet().remove(Constants.ORDER_SET, member);
        stringRedisTemplate.opsForSet().add(Constants.ORDER_SET, json);
    }

    private void maintenanceCache(int result) {
        // 维护 hotCommodity 缓存
        List<Goods> hotCommodity = goodsService.getHotCommodity();
        goodsService.maintenanceHotCommodityCache(hotCommodity, order.getGoodsId(), result);
        // 维护 getGoods 缓存
        Goods goods = goodsService.getGoods(String.valueOf(order.getGoodsId()));
        goodsService.maintenanceGoodsCache(goods, order.getGoodsId(), result);
        if (result == 0) {
            if (!Objects.isNull(stringRedisTemplate.opsForValue().get(Constants.INVENTORY + order.getGoodsId())))
                stringRedisTemplate.delete(Constants.INVENTORY + order.getGoodsId());
        }
    }

    @Override
    public void run() {

        boolean successful = true;
        //暂停程序
        pause();

        while (successful) {
            // 获取订单信息
            Set<String> set = stringRedisTemplate.opsForSet().members(Constants.ORDER_SET);
            if (!Objects.isNull(set) && !set.isEmpty()) {
                for (String temp : set) {
                    Order dbOrder = JSONObject.parseObject(temp, Order.class);
                    if (Objects.equals(dbOrder.getId(),order.getId())) {
                        if (Objects.equals(dbOrder.getState(), Order.NON_PAYMENT)) {
                            // 维护订单缓存
                            maintenanceCache(temp);
                            // 加库存
                            int result = inventoryIncrease(Constants.INVENTORY + order.getGoodsId());
                            if (result != -1) {
                                maintenanceCache(result);
                            }
                        } else {
                            successful = false;
                        }
                    }
                }
            } else {
                Order temp = orderDao.getOrder(order.getId());
                if (!Objects.isNull(temp)) {
                    if (Objects.equals(temp.getState(), Order.NON_PAYMENT)) {
                        int flag = orderDao.updateOrder(temp.getId(), Order.LOSE_EFFICACY);
                        if (flag == 1) {
                            // 加库存
                            int result = inventoryIncrease(Constants.INVENTORY + order.getGoodsId());
                            if (result != -1) {
                                maintenanceCache(result);
                            }
                        } else {
                            logger.error("修改订单状态失败，订单信息:" + order);
                        }
                    } else {
                        successful = false;
                    }
                }
            }
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                logger.error("修改订单状态失败，订单信息:" + order);
            }
        }

    }
}
