package pers.qjw.seckill.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.qjw.seckill.authorization.annotation.Authorization;
import pers.qjw.seckill.domain.ResultBody;
import pers.qjw.seckill.service.PurchaseLinkService;

@RestController
@Api(tags = "商品购买连接相关的接口")
@RequestMapping("/api/goods-url")
public class PurchaseLinkController {

    private PurchaseLinkService purchaseLinkService;

    public PurchaseLinkController(){}

    @Autowired
    public PurchaseLinkController(PurchaseLinkService purchaseLinkService) {
        this.purchaseLinkService = purchaseLinkService;
    }


    @PostMapping("/{goodsId}")
    @ApiOperation("创建一个商品购买连接")
    @Authorization
    public ResultBody createdUrl(@PathVariable String goodsId){
        return purchaseLinkService.createdUrl(goodsId);
    }

}
