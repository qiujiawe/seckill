package pers.qjw.seckill.service.impl;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.qjw.seckill.dao.UserDao;
import pers.qjw.seckill.domain.ResultDTO;
import pers.qjw.seckill.domain.User;
import pers.qjw.seckill.exception.UnableWriteToDatabaseException;
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

    public User getUser(String phone) {
        return userDao.getUser(phone);
    }

    public ResultDTO checkPhone(String phone) {
        User user = getUser(phone);
        if (Objects.isNull(user)) {
            return ResultDTO.success();
        } else {
            return ResultDTO.error("电话号码已存在");
        }
    }

    public void createUser(String phone, String password){
        String newPassword = basicTextEncryptor.encrypt(password);
        int line = userDao.insertUser(phone,newPassword);
        if (line != 1) {
            throw new UnableWriteToDatabaseException("-1","数据无法写入数据库，注册失败");
        }
    }

}
