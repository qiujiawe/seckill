package pers.qjw.seckill.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pers.qjw.seckill.dao.GoodsDao;
import pers.qjw.seckill.domain.Goods;
import pers.qjw.seckill.domain.ResultDTO;
import pers.qjw.seckill.service.GoodsService;

import java.util.List;
import java.util.Objects;

@Service
public class GoodsServiceImpl implements GoodsService {

    private GoodsDao goodsDao;

    public GoodsServiceImpl() {
    }

    @Autowired
    public GoodsServiceImpl(GoodsDao goodsDao) {
        this.goodsDao = goodsDao;
    }

    @Cacheable(cacheNames = {"hotCommodity"}, key = "caches[0].name")
    public List<Goods> hotCommodity(){
        return goodsDao.hotCommodity();
    }

    public ResultDTO getGoods(int id){
        Goods goods = goodsDao.getGoods(id);
        if (Objects.isNull(goods)) {
            return ResultDTO.error("商品编号错误");
        }
        return ResultDTO.success(goods);
    }

}
