package pers.qjw.seckill.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private Integer id;
    private String phone;
    private String password;
}
