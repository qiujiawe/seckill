package pers.qjw.seckill.service;

import pers.qjw.seckill.domain.Goods;

import java.util.List;

public interface GoodsService {
    List<Goods> getHotCommodity();

    Goods getGoods(String goodsId);

    String getLink(Goods goods);

    List<Goods> maintenanceHotCommodityCache(List<Goods> hotCommodity, int goodsId, int result);

    Goods maintenanceGoodsCache(Goods goods, int goodsId, int result);

    int setInventory(int goodsId, int inventory);
}
