package pers.qjw.seckill.timing;

import lombok.SneakyThrows;
import pers.qjw.seckill.dao.OrderDao;
import pers.qjw.seckill.dao.RedisOrderDao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class OrderTiming extends Thread{

    private final int userId;
    private final int goodsId;
    private final Timestamp createTime;
    private final RedisOrderDao redisOrderDao;
    private final OrderDao orderDao;

    public OrderTiming(int userId, int goodsId, Timestamp createTime, RedisOrderDao redisOrderDao, OrderDao orderDao){
        this.userId = userId;
        this.goodsId = goodsId;
        this.createTime = createTime;
        this.redisOrderDao = redisOrderDao;
        this.orderDao = orderDao;
    }

    @SneakyThrows
    @Override
    public void run() {
        // 订单创建的时间 - 订单过期的时间
        long left = (60 * 1000 * 5 + createTime.getTime()) - new Date().getTime() + 100;
        TimeUnit.MILLISECONDS.sleep(left);//毫秒
        if (!redisOrderDao.isPurchase(userId,goodsId)) {
            // 表示订单失效
            orderDao.updateOrder(userId,goodsId,-1);
            redisOrderDao.inventoryIncrease(goodsId);
        }
    }
}
