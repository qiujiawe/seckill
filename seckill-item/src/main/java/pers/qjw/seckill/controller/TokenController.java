package pers.qjw.seckill.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pers.qjw.seckill.annotations.Authorization;
import pers.qjw.seckill.annotations.CurrentUser;
import pers.qjw.seckill.domain.User;
import pers.qjw.seckill.service.TokenService;

/**
 * TokenController类是资源 /api/tokens 的映射类
 */
@RestController
@RequestMapping("/api/tokens")
@Api(tags = "tokens API")
public class TokenController {

    /**
     * TokenController 需要tokenService提供的业务逻辑方法做支持
     */
    private TokenService tokenService;

    /**
     * 无参构造方法
     */
    public TokenController() {
    }

    /**
     * 带参构造方法，提供给springframework让它帮我自动注入属性
     * @param tokenService 给TokenController提供业务逻辑方法
     */
    @Autowired
    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * createToken方法用来创建token并返回给前端，前端将token设置在headers里用户就不用换一个页面就登录一次了
     * @param phone 用户输入的电话号码
     * @param password 用户输入的密码
     * @return 没有异常的话会返回 200 和 token
     */
    @PostMapping
    @ApiOperation("创建token(用户登录)")
    public ResponseEntity<String> createToken(String phone, String password) {
        // 检验账号密码
        tokenService.checkPhoneAndPassword(phone,password);
        // 获取token 然后返回给前端
        return new ResponseEntity<>(tokenService.getToken(phone), HttpStatus.OK);
    }

    /**
     * deleteToken方法用于删除token
     * @param user 当前登录用户的用户信息，有@CurrentUser的支持会自动注入
     * @return 没异常会返回 200 和 成功提示
     */
    @DeleteMapping
    @Authorization
    @ApiOperation("删除token(用户登出)")
    public ResponseEntity<String> deleteToken(@CurrentUser User user) {
        tokenService.deleteToken(user.getPhone());
        return new ResponseEntity<>("注销成功",HttpStatus.OK);
    }

}
