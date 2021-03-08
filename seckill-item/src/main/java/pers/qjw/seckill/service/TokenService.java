package pers.qjw.seckill.service;

import pers.qjw.seckill.domain.ResultBody;
import pers.qjw.seckill.domain.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TokenService {

    ResultBody login(HttpServletResponse response, User user);

    void logout(HttpServletRequest request, HttpServletResponse response);
}
