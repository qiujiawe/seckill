package pers.qjw.seckill.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.qjw.seckill.authorization.annotation.Authorization;
import pers.qjw.seckill.domain.ResultBody;
import pers.qjw.seckill.domain.User;
import pers.qjw.seckill.service.TokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/tokens")
@Api(tags = "令牌相关的接口")
public class TokenController {

    private TokenService tokenService;

    public TokenController() {
    }

    @Autowired
    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    // 登录可以抽象的理解成"创建令牌"，即 post tokens
    @PostMapping
    @ApiOperation("获取令牌，即登录")
    public ResultBody login(HttpServletResponse response, User user) {
        return tokenService.login(response, user);
    }

    @DeleteMapping
    @Authorization
    // @Authorization 表示当前方法之前先检查一下用户是否已经创建令牌，如果没创建则返回 "401", "还未登录"
    @ApiOperation("删除令牌，即退出登录")
    public ResultBody logout(HttpServletRequest request, HttpServletResponse response) {
        tokenService.logout(request, response);
        return ResultBody.success("退出成功");
    }

}
