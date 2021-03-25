package pers.qjw.seckill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pers.qjw.seckill.annotations.Authorization;
import pers.qjw.seckill.annotations.CurrentUser;
import pers.qjw.seckill.domain.OrderVO;
import pers.qjw.seckill.domain.User;
import pers.qjw.seckill.service.OrderService;

import java.util.List;

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

    @GetMapping
    @Authorization
    public ResponseEntity<List<OrderVO>> listOrders(@CurrentUser User user) {
        return new ResponseEntity<>(orderService.listOrders(user.getId()), HttpStatus.OK);
    }

    @PutMapping("/{orderId}")
    @Authorization
    public ResponseEntity<String> updateOrder(@PathVariable String orderId){
        orderService.pay(orderId);
        return new ResponseEntity<>("支付成功", HttpStatus.OK);
    }
}
