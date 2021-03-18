package pers.qjw.seckill.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.qjw.seckill.annotations.CurrentUser;
import pers.qjw.seckill.domain.ResultVO;
import pers.qjw.seckill.domain.User;

import java.util.Objects;

@RestController
@RequestMapping("/api/login-status")
@Api(tags = "login-status API")
public class LoginStatusController {
    @GetMapping
    @ApiOperation("获取登录状态，用于判断用户是否登录")
    public ResultVO getLoginStatus(@CurrentUser User user){
        if (Objects.isNull(user)) {
            return ResultVO.error("-1","没登录");
        }
        return ResultVO.success("登录了");
    }
}
