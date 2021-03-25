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
    /**
     * 将 /login 映射到文件 /resources/templates/login.html 上
     * @return 返回的是文件路径(省略了一些)
     */
    @GetMapping("/login")
    @ApiOperation("login-登录页面")
    public String login(){
        return "/login";
    }

    /**
     * 将 / 映射到文件 /resources/templates/index.html 上
     * @return 返回的是文件路径(省略了一些)
     */
    @GetMapping("/")
    @ApiOperation("/-首页")
    public String index(){
        return "/index";
    }

    /**
     * 将 /register 映射到文件 /resources/templates/register.html 上
     * @return 返回的是文件路径(省略了一些)
     */
    @GetMapping("/register")
    @ApiOperation("/register-注册页面")
    public String register(){
        return "/register";
    }

    /**
     * 将 /register 映射到文件 /resources/templates/register.html 上
     * @return 返回的是文件路径(省略了一些)
     */
    @GetMapping("/goods/{goodsId}")
    @ApiOperation("/register-注册页面")
    public String goods(@PathVariable String goodsId){
        if (Strings.isNullOrEmpty(goodsId)) {
            return "/error";
        }
        return "/goods";
    }

    /**
     * 将 /register 映射到文件 /resources/templates/order.html 上
     * @return 返回的是文件路径(省略了一些)
     */
    @GetMapping("/order")
    @ApiOperation("/order-订单页面")
    public String order(){
        return "/order";
    }
}
