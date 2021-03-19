package pers.qjw.seckill.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pers.qjw.seckill.config.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@Api(tags = "提供给用户的页面")
public class PageController {

    @GetMapping("/login")
    @ApiOperation("登录页面")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    @ApiOperation("首页")
    public String index() {
        return "/index";
    }

    @GetMapping("/registered")
    @ApiOperation("注册页面")
    public String registered() {
        return "/registered";
    }

    @GetMapping("/goods/{goodsId}")
    @ApiOperation("商品页面")
    public String goods(@PathVariable String goodsId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute(Constants.GOODS_ID,goodsId);
        return "/goods";
    }

}
