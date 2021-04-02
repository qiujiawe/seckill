package pers.qjw.seckill.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pers.qjw.seckill.dao.UserDao;
import pers.qjw.seckill.interceptor.TokenInterceptor;
import pers.qjw.seckill.resolver.CurrentUserResolver;
import pers.qjw.seckill.service.TokenService;

import java.util.List;

/**
 * CertifiedComponents类用来注册拦截器,解析器
 */
@Configuration
public class CertifiedComponents implements WebMvcConfigurer {

    /**
     * TokenInterceptor 需要 TokenService 的支持
     *
     * 由于TokenInterceptor是由我构建的，所以只能把TokenService放在这里，然后在传给由于TokenInterceptor是由我构建的
     */
    private final TokenService tokenService;

    /**
     * CurrentUserResolver 需要 UserDao 的支持
     *
     * 与TokenService同理
     */
    private final UserDao userDao;

    /**
     * 构造方法 提供springframework，让springframework帮我注入对象
     * @param tokenService 提供给TokenInterceptor
     * @param userDao 提供给CurrentUserResolver
     */
    @Autowired
    public CertifiedComponents(TokenService tokenService, UserDao userDao) {
        this.tokenService = tokenService;
        this.userDao = userDao;
    }

    /**
     * 该方法用于添加拦截器
     * @param registry springframework提供的对象。用于添加拦截器，配置拦截器的功能
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 刨除掉 swagger2 相关的url，不然 swagger2 无法正常生成帮助文档
        registry.addInterceptor(new TokenInterceptor(tokenService)).addPathPatterns("/**")
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
    }

    /**
     * 该方法用于添加参数解析器
     * @param resolvers springframework提供的对象。用于添加解析器
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CurrentUserResolver(userDao));
    }
}
