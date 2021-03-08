package pers.qjw.seckill.service;

import pers.qjw.seckill.domain.ResultBody;

public interface PurchaseLinkService {
    ResultBody createdUrl(String goodsId);
}
