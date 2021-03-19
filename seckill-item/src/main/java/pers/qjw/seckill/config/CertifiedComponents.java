package pers.qjw.seckill.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pers.qjw.seckill.interceptor.TokenInterceptor;
import pers.qjw.seckill.resolver.CurrentGoodsIdResolver;
import pers.qjw.seckill.resolver.CurrentUserResolver;
import pers.qjw.seckill.service.TokenService;
import pers.qjw.seckill.service.UserService;

import java.util.List;

@Configuration
public class CertifiedComponents implements WebMvcConfigurer {

    private final TokenService tokenService;
    private final UserService userService;

    @Autowired
    public CertifiedComponents(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor(tokenService)).addPathPatterns("/**")
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CurrentUserResolver(userService));
        resolvers.add(new CurrentGoodsIdResolver());
    }
}
