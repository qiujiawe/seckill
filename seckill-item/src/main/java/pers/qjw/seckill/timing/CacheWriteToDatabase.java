package pers.qjw.seckill.timing;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pers.qjw.seckill.config.Constants;
import pers.qjw.seckill.dao.OrderDao;
import pers.qjw.seckill.domain.Order;

import java.util.Objects;
import java.util.Set;

@Component
public class CacheWriteToDatabase {

    private StringRedisTemplate stringRedisTemplate;
    private OrderDao orderDao;

    public CacheWriteToDatabase() {
    }

    @Autowired
    public CacheWriteToDatabase(StringRedisTemplate stringRedisTemplate, OrderDao orderDao) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.orderDao = orderDao;
    }

    @Scheduled(cron = "0 0/5 * * * ? ")
    @Transactional
    public void execute() {
        Set<String> orderSet = stringRedisTemplate.opsForSet().members(Constants.ORDER_SET);
        if (!Objects.isNull(orderSet) && !orderSet.isEmpty()) {
            for (String temp : orderSet) {
                stringRedisTemplate.opsForSet().remove(Constants.ORDER_SET,temp);
                Order order = JSONObject.parseObject(temp,Order.class);
                orderDao.insert(order);
            }
        }
    }

}
