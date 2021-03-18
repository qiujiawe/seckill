package pers.qjw.seckill.service;

import pers.qjw.seckill.domain.ResultDTO;
import pers.qjw.seckill.model.TokenModel;

public interface TokenService {

    ResultDTO checkPhoneAndPassword(String phone, String password);

    String createToken(String phone);

    TokenModel parsText(String text);

    ResultDTO checkToken(TokenModel tokenModel, TokenModel dbTokenModel);

    void deleteToken(String phone);

}
