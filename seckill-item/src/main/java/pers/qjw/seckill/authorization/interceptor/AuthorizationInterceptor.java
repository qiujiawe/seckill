package pers.qjw.seckill.authorization.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import pers.qjw.seckill.authorization.annotation.Authorization;
import pers.qjw.seckill.authorization.manager.TokenManager;
import pers.qjw.seckill.authorization.model.TokenModel;
import pers.qjw.seckill.config.Constant;
import pers.qjw.seckill.exception.NotLoggedInException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Objects;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final TokenManager manager;

    public AuthorizationInterceptor(TokenManager manager) {
        this.manager = manager;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        Cookie[] cookies = request.getCookies();
        // 判断是否存在cookie
        if (!Objects.isNull(cookies)) {
            // 如果cookie存在则遍历cookie
            for (Cookie temp : cookies) {
                // 判断当前cookie是否有字段的key为 Constant.TOKEN，如果有则表明该cookie是储存token字段的
                if (Objects.equals(temp.getName(), Constant.TOKEN)) {
                    // 获取cookie中token字段的value
                    String textEncryptor = temp.getValue();
                    // 解析value 获取token对象
                    TokenModel tokenModel = manager.getToken(textEncryptor);
                    // 对token对象进行验证
                    if (manager.checkToken(tokenModel)) {
                        // token与缓存中的一致
                        // 将用户id存入request作用域，方便获取
                        request.setAttribute(Constant.CURRENT_USER_ID, tokenModel.getUserId());
                        // 通过
                        return true;
                    }
                }
            }
        }
        // 通过反射获取映射方法的方法对象
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.getAnnotation(Authorization.class) == null) {
            return true;
        } else {
            throw new NotLoggedInException("-1","还没登录");
        }
    }
}
