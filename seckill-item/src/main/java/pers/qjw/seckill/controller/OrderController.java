package pers.qjw.seckill.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.qjw.seckill.authorization.annotation.Authorization;
import pers.qjw.seckill.authorization.annotation.CurrentUserId;
import pers.qjw.seckill.domain.ResultBody;
import pers.qjw.seckill.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@Api(tags = "订单相关的接口")
public class OrderController {

    private OrderService orderService;

    public OrderController() {
    }

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{text}")
    @Authorization
    @ApiOperation("创建订单")
    public ResultBody createOrder(@PathVariable String text, @CurrentUserId Integer userId) {
        return orderService.createOrder(text, userId);
    }

    @GetMapping
    @Authorization
    @ApiOperation("获取订单")
    public ResultBody listOrders(@CurrentUserId Integer userId) {
        return orderService.listOrders(userId);
    }

    @PutMapping("/{orderId}")
    @Authorization
    @ApiOperation("更新订单")
    public ResultBody payOrder(@PathVariable String orderId, @CurrentUserId Integer userId) {
        return orderService.payOrder(orderId, userId);
    }

}
