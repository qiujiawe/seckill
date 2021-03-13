package pers.qjw.seckill.dao;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;
import pers.qjw.seckill.config.Constant;
import pers.qjw.seckill.domain.Goods;
import pers.qjw.seckill.domain.Order;
import pers.qjw.seckill.exception.GoodsException;
import pers.qjw.seckill.exception.OrderException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisOrderDao {

    private StringRedisTemplate stringRedisTemplate;

    private RedisGoodsDao redisGoodsDao;

    private GoodsDao goodsDao;

    public RedisOrderDao() {
    }

    @Autowired
    public RedisOrderDao(StringRedisTemplate stringRedisTemplate, RedisGoodsDao redisGoodsDao, GoodsDao goodsDao) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisGoodsDao = redisGoodsDao;
        this.goodsDao = goodsDao;
    }

    private int performLua(String script, String key) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        Long result = stringRedisTemplate.execute(redisScript, Collections.singletonList(key));
        if (Objects.isNull(result)) {
            throw new OrderException("redis异常");
        }
        return (int) (long) result;
    }

    // 判断缓存中是否存在指定订单
    public boolean isPurchase(int userId, int goodsId) {
        // 用户id+商品id 可以当成另一个订单id
        String orderId = Constant.ORDER_ID + userId + "-" + goodsId;
        String flag = stringRedisTemplate.opsForValue().get(orderId);
        return !Objects.isNull(flag);
    }

    // 将订单id存入缓存
    public void purchaseRecords(int userId, int goodsId, Order order) {
        // 用户id+商品id 可以当成另一个订单id
        String orderId = Constant.ORDER_ID + userId + "-" + goodsId;
        stringRedisTemplate.opsForValue().set(orderId, JSONObject.toJSONString(order), 5, TimeUnit.MINUTES);
    }

    // 加库存
    public void inventoryIncrease(int goodsId) {
        // 维护Constant.INVENTORY + goodsId
        String key = Constant.INVENTORY + goodsId;
        String script = "if (redis.call('exists', KEYS[1])) then" +
                "   redis.call('incrby', KEYS[1], 1);" +
                "   return 1;" +
                "end;" +
                "return -1;";
        int intResult = performLua(script, key);
        if (intResult != 1) {
            throw new OrderException("库存增加失败");
        }
        // 维护Constant.ALL_GOODS
        String goodsListJson = stringRedisTemplate.opsForValue().get(Constant.ALL_GOODS);
        if (Objects.isNull(goodsListJson)) {
            throw new OrderException("ALL_GOODS字段不存在");
        }
        List<Goods> goodsList = JSONObject.parseArray(goodsListJson, Goods.class);
        String value = stringRedisTemplate.opsForValue().get(key);
        if (Objects.isNull(value)) {
            throw new OrderException("INVENTORY+goodsId字段不存在");
        }
        Integer number = Integer.parseInt(value);
        for (Goods item : goodsList) {
            if (item.getId() == goodsId) {
                item.setNumber(number);
            }
        }
        stringRedisTemplate.opsForValue().set(Constant.ALL_GOODS,JSONObject.toJSONString(goodsList));
        // 维护 Constant.LIST_GOODS
        String goodsJson = (String) stringRedisTemplate.opsForHash().get(Constant.LIST_GOODS,String.valueOf(goodsId));
        if (Objects.isNull(goodsJson)) {
            throw new OrderException("LIST_GOODS+goodsId字段不存在");
        }
        Goods goods = JSONObject.parseObject(goodsJson,Goods.class);
        goods.setNumber(number);
        stringRedisTemplate.opsForHash().put(Constant.LIST_GOODS,String.valueOf(goodsId),JSONObject.toJSONString(goods));
    }

    // 减库存
    public Integer inventoryReduction(int goodsId) {
        // 获取储存商品库存数量字段的key
        String key = Constant.INVENTORY + goodsId;
        // 通过key获取库存数量
        String value = stringRedisTemplate.opsForValue().get(key);
        // 判断缓存中是否储存了商品库存数量
        if (Objects.isNull(value)) {
            // 如果没有储存则添加
            Goods goods = redisGoodsDao.getGoods(goodsId);
            stringRedisTemplate.opsForValue().set(key, String.valueOf(goods.getNumber()));
        }
        String script = "if (redis.call('exists', KEYS[1])) then" +
                "     local inventory = tonumber(redis.call('get',KEYS[1]));" +
                "     if(inventory == -1) then" +
                "         return -1" +
                "     end;" +
                "     if(inventory > 0) then" +
                "         redis.call('incrby', KEYS[1], -1);" +
                "         return inventory - 1;" +
                "     end;" +
                "     return -1;" +
                "end;" +
                "return -2;";
        int intResult = performLua(script, key);
        // 如果result小于等于0 则表示商品卖完了
        if (intResult <= 0) {
            // 更新数据库
            Goods goods = goodsDao.getGoods(goodsId);
            goods.setNumber(0);
            int updateFlag = goodsDao.updateGoods(goods);
            if (updateFlag != 1) {
                throw new GoodsException("数据库更新失败");
            }
        }
        // 更新缓存中的商品信息
        redisGoodsDao.updateGoods(goodsId);
        return intResult;
    }

    // 维护缓存中订单信息
    public void maintain(Order order){
        String key = Constant.ORDER_ID + order.getUserId() + "-" + order.getGoodsId();
        stringRedisTemplate.opsForValue().set(key,JSONObject.toJSONString(order));
    }

}
