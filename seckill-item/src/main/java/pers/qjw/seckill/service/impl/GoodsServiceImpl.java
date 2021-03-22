package pers.qjw.seckill.service.impl;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pers.qjw.seckill.dao.GoodsDao;
import pers.qjw.seckill.domain.Goods;
import pers.qjw.seckill.exception.ClientDataErrorException;
import pers.qjw.seckill.exception.ConditionNotMetException;
import pers.qjw.seckill.service.GoodsService;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class GoodsServiceImpl implements GoodsService {

    private GoodsDao goodsDao;

    private BasicTextEncryptor basicTextEncryptor;

    public GoodsServiceImpl() {
    }

    @Autowired
    public GoodsServiceImpl(GoodsDao goodsDao, BasicTextEncryptor basicTextEncryptor) {
        this.goodsDao = goodsDao;
        this.basicTextEncryptor = basicTextEncryptor;
    }

    @Override
    @Cacheable(cacheNames = "HotCommodity", key = "caches[0].name")
    public List<Goods> getHotCommodity() {
        return goodsDao.getHotCommodity();
    }

    @Override
    @Cacheable(cacheNames = "goods", key = "#goodsId")
    public Goods getGoods(String goodsId) {
        int id;
        try {
            id = Integer.parseInt(goodsId);
        } catch (Exception e) {
            throw new ClientDataErrorException("错误的商品编号", HttpStatus.BAD_REQUEST);
        }
        Goods goods = goodsDao.getGoods(id);
        if (Objects.isNull(goods)) {
            throw new ClientDataErrorException("错误的商品编号", HttpStatus.BAD_REQUEST);
        }
        return goods;
    }

    @Override
    public String getLink(Goods goods) {
        long now = new Date().getTime();
        long startTime = goods.getStartTime().getTime();
        long endTime = goods.getEndTime().getTime();
        if (now >= startTime && now <= endTime) {
            String link = basicTextEncryptor.encrypt(String.valueOf(goods.getId()));
            while (link.contains("/")) {
                link = basicTextEncryptor.encrypt(String.valueOf(goods.getId()));
            }
            return "/api/orders/" + link;
        }
        throw new ConditionNotMetException("抢购时间还没到", HttpStatus.FORBIDDEN);
    }

    @Override
    @CachePut(cacheNames = "HotCommodity",key = "caches[0].name")
    public List<Goods> maintenanceHotCommodityCache(List<Goods> hotCommodity,int goodsId,int result) {
        for (Goods temp:hotCommodity) {
            if (Objects.equals(temp.getId(),goodsId)) {
                temp.setNumber(result);
            }
        }
        return hotCommodity;
    }

    @Override
    @CachePut(cacheNames = "goods", key = "#goodsId")
    public Goods maintenanceGoodsCache(Goods goods, int goodsId ,int result) {
        goods.setNumber(result);
        return goods;
    }

    @Override
    public int setInventory(int goodsId, int inventory) {
        return goodsDao.setInventory(goodsId,inventory);
    }


}
