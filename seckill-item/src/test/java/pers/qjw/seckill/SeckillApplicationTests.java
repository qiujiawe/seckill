package pers.qjw.seckill;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import pers.qjw.seckill.dao.OrderDao;
import pers.qjw.seckill.dao.TestDao;
import pers.qjw.seckill.service.TokenService;
import pers.qjw.seckill.service.impl.GoodsServiceImpl;
import pers.qjw.seckill.service.impl.OrderServiceImpl;
import pers.qjw.seckill.timing.CacheWriteToDatabase;


@SpringBootTest
class SeckillApplicationTests {

    private TokenService tokenService;
    private OrderServiceImpl orderService;
    private GoodsServiceImpl goodsService;
    private TestDao testDao;
    private CacheWriteToDatabase cacheWriteToDatabase;
    private StringRedisTemplate stringRedisTemplate;
    private OrderDao orderDao;

    @Autowired
    public SeckillApplicationTests(TokenService tokenService, OrderServiceImpl orderService, GoodsServiceImpl goodsService, TestDao testDao, CacheWriteToDatabase cacheWriteToDatabase, StringRedisTemplate stringRedisTemplate, OrderDao orderDao) {

        this.tokenService = tokenService;
        this.orderService = orderService;
        this.goodsService = goodsService;
        this.testDao = testDao;
        this.cacheWriteToDatabase = cacheWriteToDatabase;
        this.stringRedisTemplate = stringRedisTemplate;
        this.orderDao = orderDao;
    }

    @Test
    void contextLoads() {
    }

}
