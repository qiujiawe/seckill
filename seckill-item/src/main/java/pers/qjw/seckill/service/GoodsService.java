package pers.qjw.seckill.service;

import pers.qjw.seckill.domain.ResultBody;

public interface GoodsService {

    ResultBody listGoods();

    ResultBody getGoods(int goodsId);
}
