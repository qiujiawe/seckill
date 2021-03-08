package pers.qjw.seckill.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.qjw.seckill.dao.RedisGoodsDao;
import pers.qjw.seckill.domain.Goods;
import pers.qjw.seckill.domain.ResultBody;
import pers.qjw.seckill.service.GoodsService;

import java.util.Objects;

@Service
public class GoodsServiceImpl implements GoodsService {

    private RedisGoodsDao redisGoodsDao;

    public GoodsServiceImpl() {
    }

    @Autowired
    public GoodsServiceImpl(RedisGoodsDao redisGoodsDao) {
        this.redisGoodsDao = redisGoodsDao;
    }

    @Override
    public ResultBody listGoods() {
        return ResultBody.success(redisGoodsDao.listGoods());
    }

    @Override
    public ResultBody getGoods(int goodsId) {
        // 从redis中获取对象，避免数据库被多次访问
        Goods goods = redisGoodsDao.getGoods(goodsId);
        if (Objects.isNull(goods)) {
            return ResultBody.error("不存在的商品编号");
        }
        return ResultBody.success(goods);
    }

}
