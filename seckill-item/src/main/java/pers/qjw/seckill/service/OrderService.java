package pers.qjw.seckill.service;

import pers.qjw.seckill.domain.ResultBody;

public interface OrderService {

    ResultBody createOrder(String text, int userId);

    ResultBody listOrders(int userId);

    ResultBody payOrder(String userId, int UserId);
}
