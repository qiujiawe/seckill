package pers.qjw.seckill.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.qjw.seckill.authorization.annotation.GetUserId;
import pers.qjw.seckill.config.Constant;
import pers.qjw.seckill.domain.User;
import pers.qjw.seckill.domain.ResultBody;
import pers.qjw.seckill.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

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
    public ResultBody getLoginStatus(HttpServletRequest request) {
        Object flag = request.getAttribute(Constant.CURRENT_USER_ID);
        if (Objects.isNull(flag)) {
            return ResultBody.error("没登录");
        } else {
            return ResultBody.success("已登录");
        }
    }

    @PostMapping
    @ApiOperation("创建用户，即注册用户")
    public ResultBody insertUser(User user) {
        int flag = userService.insertUser(user);
        if (flag == Constant.REGISTERED_SUCCESSFULLY) {
            return ResultBody.success("注册成功");
        } else {
            return ResultBody.error("注册失败");
        }
    }

}
