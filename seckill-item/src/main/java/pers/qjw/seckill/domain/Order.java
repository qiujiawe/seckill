package pers.qjw.seckill.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Order类 用来创建储存数据的对象
 */
@Getter
@Setter
public class Order implements Serializable {
    public static final int NON_PAYMENT = 0;
    public static final int HAVE_PAID = 1;
    public static final int LOSE_EFFICACY = -1;
    private Integer id;
    private Integer state;
    private Timestamp createTime;
    private Integer goodsId;
    private Integer userId;
}
