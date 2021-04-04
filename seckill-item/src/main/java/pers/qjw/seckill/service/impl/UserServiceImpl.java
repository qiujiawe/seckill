package pers.qjw.seckill.service.impl;

import com.google.common.base.Strings;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pers.qjw.seckill.dao.UserDao;
import pers.qjw.seckill.domain.User;
import pers.qjw.seckill.exception.ClientDataErrorException;
import pers.qjw.seckill.exception.UnableWriteToDatabaseException;
import pers.qjw.seckill.service.UserService;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final BasicTextEncryptor basicTextEncryptor;

    private final UserDao userDao;

    public UserServiceImpl(BasicTextEncryptor basicTextEncryptor, UserDao userDao) {
        this.basicTextEncryptor = basicTextEncryptor;
        this.userDao = userDao;
    }

    @Override
    public User getUser(String phone, String password) {
        if (Strings.isNullOrEmpty(phone) || Strings.isNullOrEmpty(password)) {
            throw new ClientDataErrorException("用户数据不能为空", HttpStatus.BAD_REQUEST);
        }
        int phoneLength = phone.length();
        int passwordLength = password.length();
        if (phoneLength != 11 || passwordLength < 6 || passwordLength > 18) {
            throw new ClientDataErrorException("电话号码或密码有明显错误", HttpStatus.BAD_REQUEST);
        }
        return userDao.getUser(phone);
    }

    @Override
    public boolean checkPhoneAndPassword(String phone, String password) {
        User user = getUser(phone,password);
        return Objects.isNull(user);
    }

    @Override
    public void createUser(String phone, String password) {
        String encryptionPassword = basicTextEncryptor.encrypt(password);
        int flag = userDao.insertUser(phone,encryptionPassword);
        if (flag != 1) {
            throw new UnableWriteToDatabaseException("phone,password写入数据库失败",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
