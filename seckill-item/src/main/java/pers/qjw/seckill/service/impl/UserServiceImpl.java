package pers.qjw.seckill.service.impl;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.qjw.seckill.config.Constant;
import pers.qjw.seckill.dao.UserDao;
import pers.qjw.seckill.domain.User;
import pers.qjw.seckill.domain.ResultBody;
import pers.qjw.seckill.exception.UserException;
import pers.qjw.seckill.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    private BasicTextEncryptor basicTextEncryptor;

    public UserServiceImpl() {
    }

    @Autowired
    public UserServiceImpl(UserDao userDao, BasicTextEncryptor basicTextEncryptor) {
        this.userDao = userDao;
        this.basicTextEncryptor = basicTextEncryptor;
    }

    @Override
    public ResultBody getUser(HttpServletRequest request) {
        // 获取request作用域中的userid
        Object userId = request.getAttribute(Constant.CURRENT_USER_ID);
        if (Objects.isNull(userId)) {
            return ResultBody.error("还没登录");
        } else {
            return ResultBody.success("登录成功");
        }
    }

    @Override
    public ResultBody insertUser(User user) {
        // 判断用户输入的phone是否已经注册
        if (!Objects.isNull(userDao.getUser(user.getPhone()))) {
            // 已经注册
            return ResultBody.error("该电话号码已注册");
        }
        // 将用户输入的数据添加至数据库
        // 加密password
        user.setPassword(basicTextEncryptor.encrypt(user.getPassword()));
        int flag = userDao.insertUser(user);
        // 判断是否添加成功
        if (flag == 1) {
            return ResultBody.success("创建成功");
        } else {
            throw new UserException("创建失败");
        }
    }

}
