package pers.qjw.seckill.util;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class GenerateId {

    private final StringRedisTemplate stringRedisTemplate;

    private static final String GENERATE_ID = "GENERATE_ID";

    @Autowired
    public GenerateId(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    private long inventory() {
        String script = "return redis.call('incrby', KEYS[1], 1);";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        List<String> keys = new ArrayList<>();
        keys.add(GENERATE_ID);
        Long result = stringRedisTemplate.execute(redisScript, keys);
        if (!Objects.isNull(result)) {
            return result;
        } else {
            return -1;
        }
    }

    public int generateId(){
        String flag = stringRedisTemplate.opsForValue().get(GENERATE_ID);
        if (!Strings.isNullOrEmpty(flag)) {
            return (int) inventory();
        } else {
            stringRedisTemplate.opsForValue().set(GENERATE_ID,"1");
            return 1;
        }
    }

}
