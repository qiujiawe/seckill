package pers.qjw.seckill.dao;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import pers.qjw.seckill.config.Constant;
import pers.qjw.seckill.domain.Goods;
import pers.qjw.seckill.exception.GoodsException;

import java.util.List;
import java.util.Objects;

@Repository
public class RedisGoodsDao {

    private GoodsDao goodsDao;

    private StringRedisTemplate stringRedisTemplate;

    public RedisGoodsDao() {
    }

    @Autowired
    public RedisGoodsDao(GoodsDao goodsDao, StringRedisTemplate stringRedisTemplate) {
        this.goodsDao = goodsDao;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    // 获取所有商品
    public List<Goods> listGoods() {
        String listGoodsJson = stringRedisTemplate.opsForValue().get(Constant.ALL_GOODS);
        if (Strings.isNullOrEmpty(listGoodsJson)) {
            List<Goods> goodsList = goodsDao.listGoods();
            stringRedisTemplate.opsForValue().set(Constant.ALL_GOODS, JSONObject.toJSONString(goodsList));
            return goodsList;
        } else {
            return JSONObject.parseArray(listGoodsJson, Goods.class);
        }
    }

    // 获取单个商品
    public Goods getGoods(int goodsId) {
        String strGoodsId = String.valueOf(goodsId);
        Object goodsJson = stringRedisTemplate.opsForHash().get(Constant.LIST_GOODS, strGoodsId);
        if (Objects.isNull(goodsJson)) {
            // 执行到这里表示redis中没有储存对应商品信息的缓存
            // 从数据库中获取对应商品信息
            Goods goods = goodsDao.getGoods(goodsId);
            // 判断数据库中是否存在对应商品信息
            if (Objects.isNull(goods)) {
                // 没有则返回null
                return null;
            }
            // 如果数据库中有商品的信息则存入redis
            stringRedisTemplate.opsForHash().put(Constant.LIST_GOODS, strGoodsId, JSONObject.toJSONString(goods));
            // 返回对应商品信息
            return goods;
        } else {
            // 执行到这里表示redis中储存了对应商品信息的缓存
            // 将json转换成对象并返回
            return JSONObject.parseObject((String) goodsJson, Goods.class);
        }
    }

    // 更新商品库存
    public void updateGoods(int goodsId) {
        // 旧的商品对象
        Goods goods = getGoods(goodsId);
        // 新的商品库存
        String number = stringRedisTemplate.opsForValue().get(Constant.INVENTORY + goodsId);
        //
        if (Objects.isNull(number)) {
            throw new GoodsException("要设置的商品不存在");
        }
        // 修改久的商品对象，使其变成新的
        goods.setNumber(Integer.parseInt(number));
        // 用新的商品对象覆盖缓存中久对象
        stringRedisTemplate.opsForHash().put(Constant.LIST_GOODS, String.valueOf(goodsId), JSONObject.toJSONString(goods));

        List<Goods> listGoods = JSONObject.parseArray(stringRedisTemplate.opsForValue().get(Constant.ALL_GOODS), Goods.class);
        if (Objects.isNull(listGoods)) {
            return;
        }
        for (int i = 0; i < listGoods.size(); i++) {
            if (Objects.equals(listGoods.get(i).getId(), goodsId)) {
                listGoods.set(i, goods);
            }
        }
        // 用新的商品对象覆盖缓存中久对象
        stringRedisTemplate.opsForValue().set(Constant.ALL_GOODS, JSONObject.toJSONString(listGoods));
    }

}
