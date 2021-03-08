package pers.qjw.seckill.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.qjw.seckill.authorization.manager.impl.RedisTokenManager;
import pers.qjw.seckill.authorization.model.TokenModel;
import pers.qjw.seckill.config.Constant;
import pers.qjw.seckill.dao.UserDao;
import pers.qjw.seckill.domain.ResultBody;
import pers.qjw.seckill.domain.User;
import pers.qjw.seckill.exception.TokenException;
import pers.qjw.seckill.service.TokenService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    private void verification(User user) {
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
    public ResultBody login(HttpServletResponse response, User inputUser) {
        // 简单的验证一下用户输入的电话号码和密码
        verification(inputUser);
        // 根据用户提供的phone去数据中查询
        User user = userDao.getUser(inputUser.getPhone());
        // 如果 user不是null  且 密码一致
        if (!Objects.isNull(user) && Objects.equals(inputUser.getPassword(), basicTextEncryptor.decrypt(user.getPassword()))) {
            // 创建一个令牌且存入缓存
            TokenModel tokenModel = redisTokenManager.createToken(user.getId());
            // 转换成JSON
            String json = JSONObject.toJSONString(tokenModel);
            // 加密json
            String newJson = basicTextEncryptor.encrypt(json);
            // 将加密后就json存入客户端cookie
            Cookie cookie = new Cookie(Constant.TOKEN, newJson);
            // 设置cookie有效期30天
            cookie.setMaxAge(60 * 60 * 24 * 30);
            response.addCookie(cookie);
            // 返回登录成功
            return ResultBody.success(true);
        } else {
            // 返回登录失败
            return ResultBody.error("账号或密码错误");
        }
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // 获取客户端中所有的cookie
        Cookie[] cookies = request.getCookies();
        // 判断是否存在cookie
        if (!Objects.isNull(cookies)) {
            // 如果cookie存在则遍历cookie
            for (Cookie temp : cookies) {
                // 判断当前cookie是否为存放token数据的cookie
                if (Objects.equals(temp.getName(), Constant.TOKEN)) {
                    // 将存放 存放token数据的cookie 有效时间设为0 即让cookie失效
                    temp.setMaxAge(0);
                    // 将设置应用到客户端
                    response.addCookie(temp);
                }
            }
        }
        // 获取当前登录用户的id
        int userId = (Integer) request.getAttribute(Constant.CURRENT_USER_ID);
        // 根据用户的id删除redis中的缓存
        redisTokenManager.deleteToken(userId);
    }
}
