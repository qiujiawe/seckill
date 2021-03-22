package pers.qjw.seckill.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.qjw.seckill.domain.Goods;
import pers.qjw.seckill.service.GoodsService;

import java.util.List;

/**
 * GoodsController类 用来处理goods资源
 */
@RestController
@RequestMapping("/api/goods")
@Api(tags = "goods API")
public class GoodsController {

    private GoodsService goodsService;

    public GoodsController() {
    }

    @Autowired
    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @GetMapping
    @ApiOperation("获取热门商品")
    public ResponseEntity<List<Goods>> getHotCommodity() {
        return new ResponseEntity<>(goodsService.getHotCommodity(), HttpStatus.OK);
    }

    @GetMapping("/{goodsId}")
    @ApiOperation("获取单个商品")
    public ResponseEntity<Goods> getGoods(@PathVariable String goodsId) {
        return new ResponseEntity<>(goodsService.getGoods(goodsId), HttpStatus.OK);
    }

    @GetMapping("/link/{goodsId}")
    @ApiOperation("获取商品购买链接")
    public ResponseEntity<String> getLink(@PathVariable String goodsId) {
        Goods goods = goodsService.getGoods(goodsId);
        return new ResponseEntity<>(goodsService.getLink(goods), HttpStatus.OK);
    }

}
