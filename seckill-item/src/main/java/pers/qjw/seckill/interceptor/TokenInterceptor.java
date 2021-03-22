package pers.qjw.seckill.interceptor;


import com.google.common.base.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import pers.qjw.seckill.annotations.Authorization;
import pers.qjw.seckill.config.Constants;
import pers.qjw.seckill.exception.NotLoggedInException;
import pers.qjw.seckill.model.TokenModel;
import pers.qjw.seckill.service.TokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * TokenInterceptor类是springframework提供的拦截器
 * 作用一: 获取header中的token
 * 作用二: 将没访问权限的用户拦下
 */
public class TokenInterceptor implements HandlerInterceptor {

    /**
     * 获取header中的token 需要tokenService的支持
     */
    private final TokenService tokenService;

    /**
     * 构造方法
     * @param tokenService 提供给springframework自动注入
     */
    public TokenInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * 在url映射到资源方法前执行这个方法
     * @param request springframework提供的request作用域对象
     * @param response springframework提供的response作用域对象
     * @param handler url即将映射到资源方法的反射对象
     * @return true 表示放行，false 表示不给予通过
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 从 header 中得到 token
        String authorization = request.getHeader(Constants.AUTHORIZATION);
        if (!Strings.isNullOrEmpty(authorization) && !"null".equals(authorization)) {
            // 解析 token
            TokenModel token = tokenService.parsing(authorization);
            // 获取缓存中的token
            String dbToken = tokenService.getToken(token.getPhone());
            // 检验 token
            boolean flag = tokenService.checkToken(token,dbToken);
            if (flag) {
                // 用户已登录
                request.setAttribute(Constants.CURRENT_USER_PHONE,token.getPhone());
                return true;
            }
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 如果验证 token 失败，并且方法注明了 Authorization，返回 401 错误
        if (method.getAnnotation(Authorization.class) != null) {
            throw new NotLoggedInException("需要先进行登录", HttpStatus.UNAUTHORIZED);
        }

        return true;
    }

}
