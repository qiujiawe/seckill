package pers.qjw.seckill.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.qjw.seckill.authorization.annotation.Authorization;
import pers.qjw.seckill.authorization.annotation.CurrentUserId;
import pers.qjw.seckill.authorization.model.TokenModel;
import pers.qjw.seckill.config.Constant;
import pers.qjw.seckill.domain.ResultBody;
import pers.qjw.seckill.domain.User;
import pers.qjw.seckill.service.TokenService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

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
        // 校验前端发来的数据
        ResultBody verificationResult = tokenService.verification(user);
        if (!Objects.isNull(verificationResult)) {
            return verificationResult;
        }
        // 判断前端传来的phone和password是不是我们的用户
        int flagAndUserId = tokenService.isUser(user);
        if (flagAndUserId != Constant.NOT_USER) {
            // 进到这里表示 是我们的用户
            // 创建一个令牌，并存入redis
            TokenModel tokenModel = tokenService.createToken(flagAndUserId);
            // 加密令牌
            String token = tokenService.tokenEncryptor(tokenModel);
            // 将加密后就json存入客户端cookie
            Cookie cookie = new Cookie(Constant.TOKEN, token);
            // 设置cookie有效期30天
            cookie.setMaxAge(Constant.TOKEN_EXPIRATION_TIME);
            // 添加到客户端
            response.addCookie(cookie);
            // 返回登录成功
            return ResultBody.success("登录成功");
        } else {
            // 进到这里表示 不是我们的用户
            return ResultBody.error("账号或密码错误");
        }
    }

    @DeleteMapping
    @Authorization
    @ApiOperation("删除令牌，即退出登录")
    public ResultBody logout(HttpServletRequest request, HttpServletResponse response, @CurrentUserId Integer userId) {
        // 获取客户端中所有的cookie
        Cookie[] cookies = request.getCookies();
        // 判断是否存在cookie
        if (!Objects.isNull(cookies)) {
            // 如果cookie存在则遍历cookie
            for (Cookie temp : cookies) {
                // 判断当前cookie是否为存放token数据的cookie
                if (Objects.equals(temp.getName(), Constant.TOKEN)) {
                    // 将存放 存放token数据的cookie 有效时间设为0 即让cookie失效
                    temp.setMaxAge(0);
                    // 将设置应用到客户端
                    response.addCookie(temp);
                }
            }
        }
        // 删除redis中储存的token缓存
        tokenService.deleteToken(userId);
        return ResultBody.success("退出成功");
    }
}
