package pers.qjw.seckill.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    private final GoodsService goodsService;

    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    /**
     * 传给前端要展示在首页的商品列表
     * @return 商品信息列表
     */
    @GetMapping
    @ApiOperation("获取热门商品")
    public ResponseEntity<List<Goods>> getHotCommodity() {
        return new ResponseEntity<>(goodsService.getHotCommodity(), HttpStatus.OK);
    }

    /**
     * 根据前端传来的商品ID返回前端单个商品的详细信息(前端展示用)
     * @param goodsId 前端传来的商品ID
     * @return 商品ID对应的商品详细信息
     */
    @GetMapping("/{goodsId}")
    @ApiOperation("获取单个商品")
    public ResponseEntity<Goods> getGoods(@PathVariable String goodsId) {
        return new ResponseEntity<>(goodsService.getGoods(goodsId), HttpStatus.OK);
    }

    /**
     * 根据前端传来的商品ID返回前端一个随机生成的购物链接，只有通过这个链接才能购买商品
     * 随机生成是避免有用户通过程序抢购商品
     * @param goodsId 前端传来的商品ID
     * @return 随机生成的购物链接
     */
    @GetMapping("/link/{goodsId}")
    @ApiOperation("获取商品购买链接")
    public ResponseEntity<String> getLink(@PathVariable String goodsId) {
        Goods goods = goodsService.getGoods(goodsId);
        return new ResponseEntity<>(goodsService.getLink(goods), HttpStatus.OK);
    }

}
