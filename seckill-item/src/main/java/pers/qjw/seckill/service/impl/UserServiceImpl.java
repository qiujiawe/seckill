package pers.qjw.seckill.service.impl;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.qjw.seckill.dao.UserDao;
import pers.qjw.seckill.domain.User;
import pers.qjw.seckill.exception.UserException;
import pers.qjw.seckill.service.UserService;

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
    public int insertUser(User user) {
        // 判断用户输入的phone是否已经注册
        if (!Objects.isNull(userDao.getUser(user.getPhone()))) {
            // 该电话号码已经注册过
            throw new UserException("该电话号码已注册");
        }
        // 加密password
        user.setPassword(basicTextEncryptor.encrypt(user.getPassword()));
        // 将用户输入的数据添加至数据库
        return userDao.insertUser(user);
    }

}
