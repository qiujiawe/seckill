package pers.qjw.seckill.controller;

import io.swagger.annotations.ApiOperation;
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

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ApiOperation("创建订单，即购买商品")
    @PostMapping("/{randomText}")
    @Authorization
    public ResponseEntity<String> createOrder(@PathVariable String randomText, @CurrentUser User user) {
        orderService.createOrder(randomText,user.getId());
        return new ResponseEntity<>("下单成功", HttpStatus.OK);
    }

    @ApiOperation("获取用户的订单列表")
    @GetMapping
    @Authorization
    public ResponseEntity<List<OrderVO>> listOrders(@CurrentUser User user) {
        return new ResponseEntity<>(orderService.listOrders(user.getId()), HttpStatus.OK);
    }

    @ApiOperation("修改订单的支付状态码state，就是支付功能")
    @PutMapping("/{orderId}")
    @Authorization
    public ResponseEntity<String> updateOrder(@PathVariable String orderId){
        orderService.pay(orderId);
        return new ResponseEntity<>("支付成功", HttpStatus.OK);
    }
}
