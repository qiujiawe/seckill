package pers.qjw.seckill.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.qjw.seckill.authorization.manager.impl.RedisTokenManager;
import pers.qjw.seckill.authorization.model.TokenModel;
import pers.qjw.seckill.config.Constant;
import pers.qjw.seckill.dao.UserDao;
import pers.qjw.seckill.domain.User;
import pers.qjw.seckill.exception.TokenException;
import pers.qjw.seckill.service.TokenService;

import java.util.Objects;

@Service
public class TokenServiceImpl implements TokenService {

    private UserDao userDao;

    private BasicTextEncryptor basicTextEncryptor;

    private RedisTokenManager redisTokenManager;

    public TokenServiceImpl() {
    }

    @Autowired
    public TokenServiceImpl(UserDao userDao, BasicTextEncryptor basicTextEncryptor, RedisTokenManager redisTokenManager) {
        this.userDao = userDao;
        this.basicTextEncryptor = basicTextEncryptor;
        this.redisTokenManager = redisTokenManager;
    }

    @Override
    // 用来验证前端传来的数据是否正常
    public void verification(User user) {
        if (Objects.isNull(user)) {
            throw new TokenException("400", "你明明什么都没有输入，但是却能发送请求，你究竟是什么人？");
        }
        if (Objects.isNull(user.getPhone())) {
            throw new TokenException("400", "你明明没有输入电话号码，但是却能发送请求，你究竟是什么人？");
        }
        if (Objects.isNull(user.getPassword())) {
            throw new TokenException("400", "你明明没有输入密码，但是却能发送请求，你究竟是什么人？");
        }
        if (user.getPhone().length() != 11) {
            throw new TokenException("400", "你的电话号码明明不是11位，但是却能发送请求，你究竟是什么人？");
        }
        if (user.getPassword().length() < 6 || user.getPassword().length() > 18) {
            throw new TokenException("400", "你的密码明明不合规，但是却能发送请求，你究竟是什么人？");
        }
    }

    @Override
    // 判断 phone 和 password 是不是我们的用户设置的
    public int isUser(User user){
        // 拿用户电话号码去数据库中查询
        User dbUser = userDao.getUser(user.getPhone());
        if (!Objects.isNull(dbUser)) {
            // 进到这里表示数据库中有电话号码的信息
            // 比较用户输入的密码和数据库中储存的密码，一致则返回true，不一致则返回false
            if (Objects.equals(user.getPassword(), basicTextEncryptor.decrypt(dbUser.getPassword()))) {
                // 进到这里表示用户输入的密码与数据库中储存的一致 返回用户id
                return dbUser.getId();
            } else {
                return Constant.NOT_USER;
            }
        } else {
            // 进到这里表示数据库中没有该用户账号的信息，即不是用户
            return Constant.NOT_USER;
        }
    }

    @Override
    // 用 用户id 创建一个token对象，并存入redis
    public TokenModel createToken(int userId){
        return redisTokenManager.createToken(userId);
    }

    @Override
    // 加密token
    public String tokenEncryptor(TokenModel tokenModel){
        String json = JSONObject.toJSONString(tokenModel);
        return basicTextEncryptor.encrypt(json);
    }

    @Override
    // 删除redis中的token缓存
    public void deleteToken(int userId) {
        redisTokenManager.deleteToken(userId);
    }
}
