package pers.qjw.seckill.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * User类 用来创建储存数据的对象
 */
@Getter
@Setter
public class User implements Serializable {
    private Integer id;
    private String phone;
    private String password;
}
