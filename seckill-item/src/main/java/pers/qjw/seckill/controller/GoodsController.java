package pers.qjw.seckill.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.qjw.seckill.annotations.CurrentGoodsId;
import pers.qjw.seckill.domain.ResultDTO;
import pers.qjw.seckill.domain.ResultVO;
import pers.qjw.seckill.service.GoodsService;

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
    public ResultVO hotCommodity() {
        return ResultVO.success(goodsService.hotCommodity());
    }

    @GetMapping("/single")
    @ApiOperation("获取单个商品")
    public ResultVO getGoods(@CurrentGoodsId Integer goodsId){
        ResultDTO result = goodsService.getGoods(goodsId);
        if (result.isSuccess()) {
            return ResultVO.success(result.getData());
        } else {
            return ResultVO.error(result.getCode(),result.getMessage());
        }

    }

}
