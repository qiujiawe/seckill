package pers.qjw.seckill.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.qjw.seckill.annotations.CurrentUser;
import pers.qjw.seckill.domain.User;

import java.util.Objects;

@RestController
@RequestMapping("/api/login-status")
public class LoginStatusController {
    /**
     * 返回前端用户的登录状态，如果拦截器拦截到用户的TOKEN且redis中储存了该TOKEN则返回true
     * @param user 解析器自动注入的参数，不为null说明客户端中存了TOKEN
     * @return 返回一个boolean值
     */
    @ApiOperation("获取用户登录状态")
    @GetMapping
    public ResponseEntity<Boolean> getLoginStatus(@CurrentUser User user){
        return new ResponseEntity<>(!Objects.isNull(user), HttpStatus.OK);
    }
}
