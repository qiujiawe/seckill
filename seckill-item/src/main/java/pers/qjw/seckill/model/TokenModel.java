package pers.qjw.seckill.model;

import lombok.Getter;
import lombok.Setter;

/**
 * TokenModel类 用于创建token对象
 */
@Setter
@Getter
public class TokenModel {
    private String phone;
    private String uuid;

    public TokenModel() {
    }
    public TokenModel(String phone,String uuid) {
        this.phone = phone;
        this.uuid = uuid;
    }

}
