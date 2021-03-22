package pers.qjw.seckill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.qjw.seckill.annotations.Authorization;
import pers.qjw.seckill.annotations.CurrentUser;
import pers.qjw.seckill.domain.User;
import pers.qjw.seckill.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;

    public OrderController() {
    }

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{randomText}")
    @Authorization
    public ResponseEntity<String> createOrder(@PathVariable String randomText, @CurrentUser User user) {
        orderService.createOrder(randomText,user.getId());
        return new ResponseEntity<>("下单成功", HttpStatus.OK);
    }
}
