package pers.qjw.seckill.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pers.qjw.seckill.authorization.Resolver.CurrentUserIdMethodArgumentResolver;
import pers.qjw.seckill.authorization.manager.TokenManager;
import pers.qjw.seckill.authorization.interceptor.AuthorizationInterceptor;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final TokenManager manager;

    @Autowired
    public WebMvcConfig(TokenManager manager) {
        this.manager = manager;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthorizationInterceptor(manager)).excludePathPatterns(
                // 允许swagger通过
                "/swagger-resources/**",
                "/webjars/**",
                "/v2/**",
                "/swagger-ui.html/**"
        );
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new CurrentUserIdMethodArgumentResolver());
    }

}
