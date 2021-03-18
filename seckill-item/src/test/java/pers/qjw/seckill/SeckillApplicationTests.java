package pers.qjw.seckill;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pers.qjw.seckill.service.TokenService;
import pers.qjw.seckill.service.UserService;


@SpringBootTest
class SeckillApplicationTests {

    private TokenService tokenService;
    private UserService userService;

    @Autowired
    public SeckillApplicationTests(TokenService tokenService, UserService userService) {

        this.tokenService = tokenService;
        this.userService = userService;
    }

    @Test
    void contextLoads() {
        System.out.println(tokenService.createToken("17889782210"));
    }

}
