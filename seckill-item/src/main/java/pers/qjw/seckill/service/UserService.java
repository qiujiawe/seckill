package pers.qjw.seckill.service;

import pers.qjw.seckill.domain.User;
import pers.qjw.seckill.domain.ResultBody;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    ResultBody getUser(HttpServletRequest request);

    ResultBody insertUser(User user);
}
