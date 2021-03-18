package pers.qjw.seckill.service;

import pers.qjw.seckill.domain.ResultDTO;
import pers.qjw.seckill.domain.User;

public interface UserService {

    User getUser(String phone);

    ResultDTO checkPhone(String phone);

    void createUser(String phone, String password);

}
