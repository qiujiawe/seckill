package pers.qjw.seckill.config;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config/EncryptorKey.properties")
public class JasyptConfig {

    @Value("${key}")
    public String key;

    @Bean(value = "basicTextEncryptor")
    public BasicTextEncryptor basicTextEncryptor() {
        BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword(key);
        return basicTextEncryptor;
    }

}
