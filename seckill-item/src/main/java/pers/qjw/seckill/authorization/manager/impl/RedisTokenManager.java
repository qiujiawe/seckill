package pers.qjw.seckill.authorization.manager.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.util.Strings;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import pers.qjw.seckill.authorization.manager.TokenManager;
import pers.qjw.seckill.authorization.model.TokenModel;
import pers.qjw.seckill.config.Constant;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class RedisTokenManager implements TokenManager {

    private StringRedisTemplate stringRedisTemplate;

    private BasicTextEncryptor basicTextEncryptor;

    public RedisTokenManager() {
    }

    @Autowired
    public RedisTokenManager(StringRedisTemplate stringRedisTemplate, BasicTextEncryptor basicTextEncryptor) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.basicTextEncryptor = basicTextEncryptor;
    }

    @Override
    public TokenModel createToken(int userId) {
        // 生成一个随机UUID作为源token
        String token = UUID.randomUUID().toString().replace("-", "");
        TokenModel tokenModel = new TokenModel();
        tokenModel.setUserId(userId);
        tokenModel.setToken(token);
        stringRedisTemplate.opsForValue().set(Constant.USER_ID + userId, token,Constant.TOKEN_EXPIRATION_TIME, TimeUnit.SECONDS);
        return tokenModel;
    }

    @Override
    public boolean checkToken(TokenModel model) {
        // 查询redis中是否存在用户令牌缓存
        String token = stringRedisTemplate.opsForValue().get(Constant.USER_ID + model.getUserId());
        // 如果redis中存在用户令牌缓存且令牌与一致则返回true，否则返回false
        return !Strings.isEmpty(token) && Objects.equals(token, model.getToken());
    }

    @Override
    public TokenModel getToken(String authentication) {
        // 还原加密的字符串
        String json = basicTextEncryptor.decrypt(authentication);
        // 将字符串转换成对象后返回
        return JSONObject.parseObject(json,TokenModel.class);
    }

    @Override
    public void deleteToken(int userId) {
        // 删除缓存
        stringRedisTemplate.delete(Constant.USER_ID + userId);
    }
}
