package pers.qjw.seckill.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import pers.qjw.seckill.annotations.CurrentUser;
import pers.qjw.seckill.config.Constants;
import pers.qjw.seckill.domain.User;
import pers.qjw.seckill.service.UserService;

import java.util.Objects;

public class CurrentUserResolver implements HandlerMethodArgumentResolver {

    private final UserService userService;

    public CurrentUserResolver(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().isAssignableFrom(User.class) &&
                methodParameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) {
        // 取出鉴权时存入的登录用户 Id
        String currentUserPhone = (String) nativeWebRequest.getAttribute (Constants.PHONE, RequestAttributes.SCOPE_REQUEST);
        if (!Objects.isNull(currentUserPhone)) {
            // 从数据库中查询并返回
            return userService.getUser(currentUserPhone);
        }
        return null;
    }
}
