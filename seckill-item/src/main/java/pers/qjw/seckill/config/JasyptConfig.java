package pers.qjw.seckill.config;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * JasyptConfig 是加密工具Jasypt的配置类
 */
@Configuration
@PropertySource("classpath:config/EncryptorKey.properties")
public class JasyptConfig {

    /**
     * 这个属性从配置文件中读取 "盐" ，然后提供springframework创建basicTextEncryptor对象
     */
    @Value("${key}")
    public String key;

    /**
     * 让springframework管理BasicTextEncryptor对象，需要是使用自动注入会很方便
     * @return 返回的是设置好 "盐" 的BasicTextEncryptor对象
     */
    @Bean(value = "basicTextEncryptor")
    public BasicTextEncryptor basicTextEncryptor() {
        BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword(key);
        return basicTextEncryptor;
    }

}
