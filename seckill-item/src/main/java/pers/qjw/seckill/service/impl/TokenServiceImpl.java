package pers.qjw.seckill.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pers.qjw.seckill.domain.ResultDTO;
import pers.qjw.seckill.domain.User;
import pers.qjw.seckill.model.TokenModel;
import pers.qjw.seckill.service.TokenService;
import pers.qjw.seckill.service.UserService;

import java.util.Objects;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    private UserService userService;

    private BasicTextEncryptor basicTextEncryptor;

    public TokenServiceImpl() {
    }

    @Autowired
    public TokenServiceImpl(UserService userService, BasicTextEncryptor basicTextEncryptor) {
        this.userService = userService;
        this.basicTextEncryptor = basicTextEncryptor;
    }

    @Override
    // 删除用户之前的token(如果有的话)
    @CacheEvict(cacheNames = "tokens", key = "#phone")
    // 校验电话号码和密码
    public ResultDTO checkPhoneAndPassword(String phone, String password) {
        // 用phone去数据库中查询
        User user = userService.getUser(phone);
        if (Objects.isNull(user)) {
            // 查询不到
            return ResultDTO.error("账号或密码错误");
        }
        // 解析数据库中加密的密码
        String passwordAnalyticalResults = basicTextEncryptor.decrypt(user.getPassword());
        // 用解析出来的密码 去和 用户输入的密码 比较
        if (!Objects.equals(password, passwordAnalyticalResults)) {
            // 密码不一致
            return ResultDTO.error("账号或密码错误");
        }
        return ResultDTO.success();
    }

    @Override
    // 将加密后的token对象JSON存入缓存
    @Cacheable(cacheNames = "tokens", key = "#phone")
    // 创建token
    public String createToken(String phone) {
        // 创建一个token对象
        TokenModel tokenModel = new TokenModel();
        // 设置电话号码
        tokenModel.setPhone(phone);
        // 设置UUID
        tokenModel.setUuid(UUID.randomUUID().toString().replace("-", ""));
        // 转成JSON
        String tokenJson = JSONObject.toJSONString(tokenModel);
        // 加密后返回
        return basicTextEncryptor.encrypt(tokenJson);
    }

    @Override
    // 解析token
    public TokenModel parsText(String text) {
        String tokenJson = basicTextEncryptor.decrypt(text);
        return JSONObject.parseObject(tokenJson, TokenModel.class);
    }

    @Override
    // 校验token
    public ResultDTO checkToken(TokenModel tokenModel, TokenModel dbTokenModel) {
        // 比较两个对象
        if (tokenModel.getPhone().equals(dbTokenModel.getPhone()) &&
                tokenModel.getUuid().equals(dbTokenModel.getUuid())
        ) {
            // 一致
            return ResultDTO.success(dbTokenModel.getPhone());
        }
        return ResultDTO.error("校验失败");
    }

    @CacheEvict(cacheNames = "tokens", key = "#phone")
    // 删除token
    public void deleteToken(String phone) {
    }

}
