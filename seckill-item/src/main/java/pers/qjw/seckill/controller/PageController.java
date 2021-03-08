package pers.qjw.seckill.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
@Api(tags = "页面路径")
public class PageController {

    @GetMapping("/login")
    @ApiOperation("登录页面")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    @ApiOperation("注册页面")
    public String register(){
        return "register";
    }

    @GetMapping("/")
    @ApiOperation("首页，同时也是商品页面")
    public String index(){
        return "index";
    }

    @GetMapping("/purchase/{goodsId}")
    @ApiOperation("购买页面")
    public String purchase(@PathVariable int goodsId, HttpServletRequest request){
        request.getSession().setAttribute("goodsId",goodsId);
        return "purchase";
    }

    @GetMapping("/order")
    @ApiOperation("订单页面")
    public String order(){
        return "order";
    }

}
