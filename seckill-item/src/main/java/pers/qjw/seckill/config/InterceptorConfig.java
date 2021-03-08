package pers.qjw.seckill.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pers.qjw.seckill.authorization.manager.TokenManager;
import pers.qjw.seckill.interceptor.AuthorizationInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final TokenManager manager;

    @Autowired
    public InterceptorConfig(TokenManager manager) {
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

}
