package pers.qjw.seckill.service;

import pers.qjw.seckill.model.TokenModel;

public interface TokenService {
    String getToken(String phone);

    void deleteToken(String phone);

    void checkPhoneAndPassword(String phone, String password);

    TokenModel parsing(String authorization);

    boolean checkToken(TokenModel token, String dbToken);
}
