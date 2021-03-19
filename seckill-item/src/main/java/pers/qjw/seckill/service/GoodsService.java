package pers.qjw.seckill.service;

import pers.qjw.seckill.domain.Goods;
import pers.qjw.seckill.domain.ResultDTO;

import java.util.List;

public interface GoodsService {

    List<Goods> hotCommodity();

    ResultDTO getGoods(int id);

}
