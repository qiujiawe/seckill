package pers.qjw.seckill.interceptor;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import pers.qjw.seckill.annotations.Authorization;
import pers.qjw.seckill.config.Constants;
import pers.qjw.seckill.domain.ResultDTO;
import pers.qjw.seckill.exception.NotLoggedInException;
import pers.qjw.seckill.model.TokenModel;
import pers.qjw.seckill.service.TokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Objects;

public class TokenInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;

    public TokenInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // 从 header 中得到 token
        String text = request.getHeader(Constants.AUTHORIZATION);
        // 判断token是否存在
        if (!Objects.isNull(text) && !"null".equals(text)) {
            // 解析token
            TokenModel tokenModel = tokenService.parsText(text);
            // 获取redis中储存的token
            String json = tokenService.createToken(tokenModel.getPhone());
            // 解析
            TokenModel dbTokenModel = tokenService.parsText(json);
            // 校验token
            ResultDTO result = tokenService.checkToken(tokenModel,dbTokenModel);
            if (result.isThrough()) {
                // 校验成功, 将当前用户的phone存入request作用域
                request.setAttribute(Constants.PHONE,result.getData());
                return true;
            }
        }
        // 获取反射对象
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod ();
        // 判断方法上有没有Authorization注解
        if (method.getAnnotation (Authorization.class) != null) {
            // 有Authorization注解
            throw new NotLoggedInException("401","还没登录");
        } else {
            return true;
        }

    }

}
