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

    private String getText(Integer goodsId){
        String text;
        for (text = basicTextEncryptor.encrypt(String.valueOf(goodsId)); text.contains("/");) {
            text = basicTextEncryptor.encrypt(String.valueOf(goodsId));
        }
        return text;
    }

    @Override
    public ResultBody createdUrl(Integer goodsId) {
        if (Objects.isNull(goodsId)) {
            throw new GoodsException("商品编号不存在");
        }
        Goods goods = redisGoodsDao.getGoods(goodsId);
        // 判断商品是否在抢购期间
        long startTime = goods.getStartTime().getTime();
        long endTime = goods.getEndTime().getTime();
        long nowTime = new Date().getTime();
        if (nowTime > startTime && endTime > nowTime) {
            // 活动时间
            return ResultBody.success("/api/orders/" + getText(goodsId));
        } else {
            // 非活动时间
            return ResultBody.error("抢购未开始");
        }

    }
}
