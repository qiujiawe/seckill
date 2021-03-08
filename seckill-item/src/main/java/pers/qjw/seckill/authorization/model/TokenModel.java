package pers.qjw.seckill.authorization.model;

import lombok.Data;

@Data
public class TokenModel {
    private int userId;
    private String token;
}
