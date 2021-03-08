package pers.qjw.seckill;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import pers.qjw.seckill.dao.*;
import pers.qjw.seckill.domain.Goods;
import pers.qjw.seckill.domain.User;

import java.util.List;
import java.util.Objects;

@SpringBootTest
class SeckillApplicationTests {

    private final UserDao userDao;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisGoodsDao redisGoodsDao;
    private final RedisOrderDao redisOrderDao;
    private final GoodsDao goodsDao;
    private final OrderDao orderDao;

    @Autowired
    public SeckillApplicationTests(UserDao userDao, StringRedisTemplate stringRedisTemplate, RedisGoodsDao redisGoodsDao, RedisOrderDao redisOrderDao, GoodsDao goodsDao, OrderDao orderDao) {
        this.userDao = userDao;
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisGoodsDao = redisGoodsDao;
        this.redisOrderDao = redisOrderDao;
        this.goodsDao = goodsDao;
        this.orderDao = orderDao;
    }

    @Test
    void contextLoads() {

    }

    @Test
    void t1() {
        // userDao测试
        User testUser = new User();
        testUser.setPhone("164615641");
        testUser.setPassword("16545165");
        System.out.println(userDao.insertUser(testUser));
    }

    @Test
    void t2() {
        // JSONObject测试
        User user = new User();
        user.setPhone("17889782210");
        user.setPassword("123456");
        String json = JSONObject.toJSONString(user);
        System.out.println(json);
        User uset1 = JSONObject.parseObject(json, User.class);
        System.out.println(uset1);
    }

    @Test
    void t3() {
        // redis储存hash测试
        Goods goods = goodsDao.getGoods(1);
        String json = JSONObject.toJSONString(goods);
        stringRedisTemplate.opsForHash().put("listGoods","1",json);
        String goods1 = (String) stringRedisTemplate.opsForHash().get("listGoods","1");
        Goods goods2 = JSONObject.parseObject(goods1,Goods.class);
        System.out.println(goods2);
        Object test = stringRedisTemplate.opsForHash().get("listGoods", "2");
        System.out.println(Objects.isNull(test));
    }

    @Test
    void t4() {
        // redisGoodsDao测试
        List<Goods> list = redisGoodsDao.listGoods();
        System.out.println(list);
    }

    @Test
    void t5() {
        // 在redis中储存 listOrder - UserId - GoodsId
        String value = "1" + "-" + "1";
        stringRedisTemplate.opsForSet().add("listOrder",value);
        // 获取
        Boolean flag = stringRedisTemplate.opsForSet().isMember("listOrder","value");
        System.out.println(flag);
    }

    @Test
    void t6() {
        // 测试缓存变量自减
        System.out.println(redisOrderDao.inventoryReduction(1));
    }

    @Test
    void t7() {
        // 测试 orderDao
        System.out.println(orderDao.listOrders(5));
    }

    @Test
    void t8() {
        // 测试 inventoryIncrease
        redisOrderDao.inventoryIncrease(1);
    }


}
