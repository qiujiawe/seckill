package pers.qjw.seckill.service;

import pers.qjw.seckill.domain.User;

public interface UserService {
    User getUser(String phone, String password);

    boolean checkPhoneAndPassword(String phone, String password);

    void createUser(String phone, String password);
}
