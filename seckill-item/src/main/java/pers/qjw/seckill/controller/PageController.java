package pers.qjw.seckill.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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

}
