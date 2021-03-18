package pers.qjw.seckill.domain;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class Order implements Serializable {
    private Integer id;
    private Integer state;
    private Timestamp createTime;
    private Integer goodsId;
    private Integer userId;
}
