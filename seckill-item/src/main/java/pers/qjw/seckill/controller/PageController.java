package pers.qjw.seckill.controller;

import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * PageController类用来将做文件映射，就是将url映射到动态资源上
 */
@Api(tags = "地址与动态资源的映射关系")
@Controller
public class PageController {

    @GetMapping("/login")
    @ApiOperation("登录页面")
    public String login(){
        return "/login";
    }

    @GetMapping("/")
    @ApiOperation("首页")
    public String index(){
        return "/index";
    }

    @GetMapping("/register")
    @ApiOperation("注册页面")
    public String register(){
        return "/register";
    }

    @GetMapping("/goods/{goodsId}")
    @ApiOperation("注册页面")
    public String goods(@PathVariable String goodsId){
        if (Strings.isNullOrEmpty(goodsId)) {
            return "/error";
        }
        return "/goods";
    }

    @GetMapping("/order")
    @ApiOperation("订单页面")
    public String order(){
        return "/order";
    }
}
