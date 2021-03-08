package pers.qjw.seckill.service;

import pers.qjw.seckill.domain.ResultBody;

public interface OrderService {

    ResultBody createOrder(String text, Integer userId);

    ResultBody listOrders(Integer userId);
}
