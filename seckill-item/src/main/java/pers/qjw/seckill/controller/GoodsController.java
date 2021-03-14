package pers.qjw.seckill.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.qjw.seckill.authorization.annotation.Authorization;
import pers.qjw.seckill.domain.ResultBody;
import pers.qjw.seckill.service.GoodsService;

@RestController
@RequestMapping("/api/goods")
@Api(tags = "商品相关的接口")
public class GoodsController {

    private GoodsService goodsService;

    public GoodsController() {
    }

    @Autowired
    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @GetMapping
    @ApiOperation("获取所有商品信息")
    public ResultBody listGoods() {
        return ResultBody.success(goodsService.listGoods());
    }

    @GetMapping("/{goodsId}")
    @Authorization
    @ApiOperation("获取单个商品信息")
    public ResultBody getGoods(@PathVariable String goodsId) {
        return ResultBody.success(goodsService.getGoods(goodsId));
    }

}
