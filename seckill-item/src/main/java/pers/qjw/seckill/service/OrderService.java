package pers.qjw.seckill.service;

import pers.qjw.seckill.domain.OrderVO;

import java.util.List;

public interface OrderService {
    void createOrder(String randomText, Integer id);

    List<OrderVO> listOrders(Integer id);

    void pay(String orderId);
}
