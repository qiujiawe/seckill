package pers.qjw.seckill.authorization.manager;

import pers.qjw.seckill.authorization.model.TokenModel;

/**
 * 对Token进行操作的接口
 */
public interface TokenManager {

    /**
     * 创建一个token关联上指定用户
     */
    TokenModel createToken(int userId);

    /**
     * 检查token是否有效
     */
    boolean checkToken(TokenModel model);

    /**
     * 从字符串中解析token
     */
    TokenModel getToken(String authentication);

    /**
     * 清除token
     */
    void deleteToken(int userId);

}
