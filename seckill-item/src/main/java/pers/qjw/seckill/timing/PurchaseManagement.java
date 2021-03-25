package pers.qjw.seckill.timing;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pers.qjw.seckill.exception.ConditionNotMetException;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class PurchaseManagement {

    private final StringRedisTemplate stringRedisTemplate;

    public PurchaseManagement(StringRedisTemplate stringRedisTemplate){

        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void bought(int userId, int goodsId) {
        String key = userId + "-" + goodsId;
        String value = stringRedisTemplate.opsForValue().get(key);
        if (!Objects.isNull(value)) {
            throw new ConditionNotMetException("5分钟内只能下单一次", HttpStatus.FORBIDDEN);
        }
    }

    public void addRecords(int userId,int goodsId){
        stringRedisTemplate.opsForValue().set(userId + "-" + goodsId, "#", 5, TimeUnit.MINUTES);
    }


}
