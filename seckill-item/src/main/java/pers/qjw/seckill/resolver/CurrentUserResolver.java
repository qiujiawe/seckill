package pers.qjw.seckill.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import pers.qjw.seckill.annotations.CurrentUser;
import pers.qjw.seckill.config.Constants;
import pers.qjw.seckill.dao.UserDao;
import pers.qjw.seckill.domain.User;

/**
 * CurrentUserResolver类 是方法参数解析器
 */
public class CurrentUserResolver implements HandlerMethodArgumentResolver {

    /**
     * 解析器需要UserDao的支持，REQUEST作用域对象里只有用户的phone，其他信息要通过userDao从数据库里查
     */
    private final UserDao userDao;

    /**
     * 构造方法，提供给CertifiedComponents类方便创建对象
     * @param userDao 持久层对象
     */
    public CurrentUserResolver(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * supportsParameter方法表示什么时候往参数里注入数据
     * @param methodParameter springframework提供的对象，
     * @return true表示注入
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        // 如果参数类型为 User 并且有 CurrentUser 注解则注入
        return methodParameter.getParameterType().isAssignableFrom(User.class) &&
                methodParameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) {
        // 取出拦截器(TokenInterceptor类)存在REQUEST作用域里的用户phone
        String currentUserPhone = (String) nativeWebRequest.getAttribute(Constants.CURRENT_USER_PHONE, RequestAttributes.SCOPE_REQUEST);
        if (currentUserPhone != null) {
            // 从数据库中查询并返回
            return userDao.getUser(currentUserPhone);
        }
        return null;
    }
}
