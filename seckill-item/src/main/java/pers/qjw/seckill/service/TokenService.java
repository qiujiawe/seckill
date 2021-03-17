package pers.qjw.seckill.service;

import pers.qjw.seckill.authorization.model.TokenModel;
import pers.qjw.seckill.domain.ResultBody;
import pers.qjw.seckill.domain.User;

public interface TokenService {

    ResultBody verification(User user);

    int isUser(User user);

    TokenModel createToken(int userId);

    String tokenEncryptor(TokenModel tokenModel);

    void deleteToken(int userId);

}
