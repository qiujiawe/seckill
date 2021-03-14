package pers.qjw.seckill.service;

import pers.qjw.seckill.domain.Goods;

import java.util.List;

public interface GoodsService {

    List<Goods> listGoods();

    Goods getGoods(String goodsId);
}
