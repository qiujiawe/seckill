package pers.qjw.seckill.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pers.qjw.seckill.dao.UserDao;
import pers.qjw.seckill.domain.User;
import pers.qjw.seckill.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    public UserServiceImpl() {
    }

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }


    @Cacheable(cacheNames = "users",key = "#phone")
    public User getUser(String phone){
        return userDao.getUser(phone);
    }

}
