package pers.qjw.seckill.service.impl;

import com.google.common.base.Strings;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.qjw.seckill.dao.OrderDao;
import pers.qjw.seckill.dao.RedisGoodsDao;
import pers.qjw.seckill.dao.RedisOrderDao;
import pers.qjw.seckill.domain.Goods;
import pers.qjw.seckill.domain.Order;
import pers.qjw.seckill.domain.ResultBody;
import pers.qjw.seckill.service.OrderService;
import pers.qjw.seckill.timing.OrderTiming;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class OrderServiceImpl implements OrderService {

    private BasicTextEncryptor basicTextEncryptor;

    private RedisGoodsDao redisGoodsDao;

    private OrderDao orderDao;

    private RedisOrderDao redisOrderDao;

    public OrderServiceImpl() {
    }

    @Autowired
    public OrderServiceImpl(BasicTextEncryptor basicTextEncryptor, RedisGoodsDao redisGoodsDao, OrderDao orderDao, RedisOrderDao redisOrderDao) {
        this.basicTextEncryptor = basicTextEncryptor;
        this.redisGoodsDao = redisGoodsDao;
        this.orderDao = orderDao;
        this.redisOrderDao = redisOrderDao;
    }

    @Override
    @Transactional
    public ResultBody createOrder(String text, Integer userId) {
        if (Strings.isNullOrEmpty(text)) {
            return ResultBody.error("未接收到加密字段");
        }
        String goodsId = basicTextEncryptor.decrypt(text);
        Goods goods = redisGoodsDao.getGoods(Integer.parseInt(goodsId));
        if (Objects.isNull(goods)) {
            // 商品不存在 无法创建订单
            return ResultBody.error("加密字段错误");
        } else {
            // 判断当前用户是否已购买过此商品
            boolean redisFlag = redisOrderDao.isPurchase(userId, goods.getId());
            if (redisFlag) {
                return ResultBody.error("已购买过此商品");
            }
            // 减库存
            Integer inventory = redisOrderDao.inventoryReduction(goods.getId());
            // inventory 的值表示什么
            // -2 表示商品不存在
            // -1 表示库存数量等于 -1 或 小于 -1 通常来说不应该出现-1
            // 0及0以上 表示库存正常的自减了
            if (inventory >= 0) {
                // 创建订单
                Order order = new Order();
                order.setUserId(userId);
                order.setGoodsId(goods.getId());
                order.setCreateTime(new Timestamp(new Date().getTime()));
                // 0 表示订单未支付
                order.setState(0);
                // 将订单信息添加进数据库
                int flag = orderDao.insertOrder(order);
                // 记录 用户购买商品的信息 ，用于判断当前用户是否已购买过此商品
                redisOrderDao.purchaseRecords(userId, goods.getId(), order);
                // 判断是否成功添加进数据库
                if (flag == 1) {
                    // 开启一个线程，5分钟后检查订单是否被支付
                    OrderTiming orderTiming = new OrderTiming(userId, goods.getId(), order.getCreateTime(), redisOrderDao, orderDao);
                    orderTiming.start();
                    return ResultBody.success("订单创建成功");
                } else {
                    return ResultBody.error("订单创建失败");
                }
            } else {
                return ResultBody.error("商品已经卖完了");
            }

        }
    }

    @Override
    public ResultBody listOrders(Integer userId) {
        return ResultBody.success(orderDao.listOrders(userId));
    }

    @Override
    public ResultBody payOrder(String orderId, int userId) {
        int intOrderId;
        try {
            intOrderId = Integer.parseInt(orderId);
        } catch (Exception e) {
            return ResultBody.error("订单编号异常");
        }
        List<Order> orderList = orderDao.listOrders(userId);
        for (Order temp : orderList) {
            if (temp.getId() == intOrderId) {
                // 订单属于当前用户
                if (temp.getState().equals(Order.NON_PAYMENT)) {
                    // 未支付
                    temp.setState(Order.PAID);
                    // 维护缓存与数据库中的订单信息
                    redisOrderDao.maintain(temp);
                    orderDao.updateOrderWay2(temp.getId(), Order.PAID);
                    return ResultBody.success("支付成功");
                } else if (temp.getState().equals(Order.OVERTIME)) {
                    return ResultBody.error("订单已失效");
                } else {
                    return ResultBody.error("订单已支付");
                }
            }
        }
        return ResultBody.error("订单编号异常");
    }

}
