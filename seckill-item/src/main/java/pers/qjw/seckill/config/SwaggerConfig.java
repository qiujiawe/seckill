package pers.qjw.seckill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                // select 用于设置 apis and paths
                .select()
                // apis 用于设置生成那些 controller 内的接口
                .apis(RequestHandlerSelectors.any())
                // path 用于设置生成那些具体的接口
                .paths(PathSelectors.any())
                .build()
                .apiInfo(
                        new ApiInfoBuilder()
                                .title("seckill")
                                .description("seckill项目api")
                                .version("1.0")
                                .build()
                );
    }
}
