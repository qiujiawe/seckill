package pers.qjw.seckill.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.qjw.seckill.authorization.annotation.GetUserId;
import pers.qjw.seckill.domain.User;
import pers.qjw.seckill.domain.ResultBody;
import pers.qjw.seckill.service.UserService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/users")
@Api(tags = "用户相关的接口")
public class UserController {

    private UserService userService;

    public UserController() {
    }

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login-state")
    @ApiOperation("获取用户登录状态，如果已登录返回true")
    @GetUserId
    public ResultBody getUser(HttpServletRequest request) {
        return userService.getUser(request);
    }

    @PostMapping
    @ApiOperation("创建用户，即注册用户")
    public ResultBody insertUser(User user) {
        return userService.insertUser(user);
    }

}
