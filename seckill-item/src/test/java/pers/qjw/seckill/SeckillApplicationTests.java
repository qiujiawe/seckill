package pers.qjw.seckill;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pers.qjw.seckill.service.GoodsService;
import pers.qjw.seckill.service.TokenService;
import pers.qjw.seckill.service.UserService;


@SpringBootTest
class SeckillApplicationTests {

    private TokenService tokenService;
    private UserService userService;
    private GoodsService goodsService;

    @Autowired
    public SeckillApplicationTests(TokenService tokenService, UserService userService, GoodsService goodsService) {

        this.tokenService = tokenService;
        this.userService = userService;
        this.goodsService = goodsService;
    }

    @Test
    void contextLoads() {
        System.out.println(goodsService.hotCommodity());
    }

}
