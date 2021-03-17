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
    public ResultBody hotCommodity() {
        return ResultBody.success(redisGoodsDao.hotCommodity());
    }

    @Override
    public ResultBody getGoods(String goodsId) {
        // 判断前端传来的字符串是否能正常装换成int
        int intGoodsId;
        try {
            intGoodsId = Integer.parseInt(goodsId);
        } catch (Exception e) {
            return ResultBody.error("不正常的商品编号");
        }
        // 通过goodsId从redis中获取对象，避免数据库被多次访问
        Goods goods = redisGoodsDao.getGoods(intGoodsId);
        // 判断是否能获取到
        if (Objects.isNull(goods)) {
            return ResultBody.error("错误的商品编号");
        }
        return ResultBody.success(goods);
    }

}
