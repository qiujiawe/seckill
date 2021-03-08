package pers.qjw.seckill.service.impl;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.qjw.seckill.dao.RedisGoodsDao;
import pers.qjw.seckill.domain.Goods;
import pers.qjw.seckill.domain.ResultBody;
import pers.qjw.seckill.exception.GoodsException;
import pers.qjw.seckill.service.PurchaseLinkService;

import java.util.Date;
import java.util.Objects;

@Service
public class PurchaseLinkServiceImpl implements PurchaseLinkService {

    private BasicTextEncryptor basicTextEncryptor;

    private RedisGoodsDao redisGoodsDao;

    public PurchaseLinkServiceImpl() {
    }

    @Autowired
    public PurchaseLinkServiceImpl(BasicTextEncryptor basicTextEncryptor, RedisGoodsDao redisGoodsDao) {
        this.basicTextEncryptor = basicTextEncryptor;
        this.redisGoodsDao = redisGoodsDao;
    }

    private String getText(int goodsId) {
        String text;
        String strGoodsId = String.valueOf(goodsId);
        for (text = basicTextEncryptor.encrypt(strGoodsId); text.contains("/"); ) {
            text = basicTextEncryptor.encrypt(strGoodsId);
        }
        return text;
    }

    @Override
    public ResultBody createdUrl(String goodsId) {
        // 判断前端是否传来商品编号字符串
        if (Objects.isNull(goodsId)) {
            throw new GoodsException("商品编号不存在");
        }
        // 判断前端传来的商品编号字符串是否能转换成int
        int intGoodsId;
        try {
            intGoodsId = Integer.parseInt(goodsId);
        } catch (Exception e) {
            throw new GoodsException("错误的商品编号");
        }

        Goods goods = redisGoodsDao.getGoods(intGoodsId);
        // 判断商品是否在抢购期间
        long startTime = goods.getStartTime().getTime();
        long endTime = goods.getEndTime().getTime();
        long nowTime = new Date().getTime();
        if (nowTime > startTime && endTime > nowTime) {
            // 活动时间
            return ResultBody.success("/api/orders/" + getText(intGoodsId));
        } else {
            // 非活动时间
            return ResultBody.error("抢购未开始");
        }

    }
}
