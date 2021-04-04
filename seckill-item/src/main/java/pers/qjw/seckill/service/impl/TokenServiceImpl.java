package pers.qjw.seckill.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pers.qjw.seckill.domain.User;
import pers.qjw.seckill.exception.ClientDataErrorException;
import pers.qjw.seckill.model.TokenModel;
import pers.qjw.seckill.service.TokenService;
import pers.qjw.seckill.service.UserService;

import java.util.Objects;
import java.util.UUID;

/**
 * Token业务层的具体实现类
 */
@Service
public class TokenServiceImpl implements TokenService {

    private final UserService userService;

    private final BasicTextEncryptor basicTextEncryptor;

    public TokenServiceImpl(UserService userService, BasicTextEncryptor basicTextEncryptor) {
        this.userService = userService;
        this.basicTextEncryptor = basicTextEncryptor;
    }

    /**
     * 检验用户输入的phone和password能否创建token
     * @param phone 用户输入的phone
     * @param password 用户输入的password
     */
    @Override
    @CacheEvict(cacheNames = "tokens", key = "#phone")
    public void checkPhoneAndPassword(String phone, String password) {
        User user = userService.getUser(phone,password);
        if (Objects.isNull(user)) {
            throw new ClientDataErrorException("电话号码或密码错误", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 将加密后的字符串解析成TokenModel对象
     * @param authorization 加密后的字符串
     * @return TokenModel对象
     */
    @Override
    public TokenModel parsing(String authorization) {
        String json = basicTextEncryptor.decrypt(authorization);
        return JSONObject.parseObject(json, TokenModel.class);
    }

    /**
     * 检验客户端传来的token是否与缓存中的一致
     * @param token 客户端传来的token
     * @param dbToken 缓存中的token
     * @return 校验结果
     */
    @Override
    public boolean checkToken(TokenModel token, String dbToken) {
        String json = basicTextEncryptor.decrypt(dbToken);
        TokenModel newToken = JSONObject.parseObject(json, TokenModel.class);
        return Objects.equals(token.getPhone(),newToken.getPhone()) &&
                Objects.equals(token.getUuid(),newToken.getUuid());
    }

    /**
     * 获取token，如果没有则创建后返回
     * @param phone 电话号码
     * @return 加密后的tokenModel对象JSON字符串
     */
    @Override
    @Cacheable(cacheNames = "tokens", key = "#phone")
    public String getToken(String phone) {
        TokenModel tokenModel = new TokenModel(phone,UUID.randomUUID().toString().replace("-", ""));
        String json = JSONObject.toJSONString(tokenModel);
        return basicTextEncryptor.encrypt(json);
    }

    /**
     * 删除缓存中的token
     * @param phone 做为缓存的key提供给cache注解
     */
    @Override
    @CacheEvict(cacheNames = "tokens", key = "#phone")
    public void deleteToken(String phone) {
    }
}
