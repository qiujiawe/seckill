package pers.qjw.seckill.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Goods类 用来创建储存数据的对象
 */
@Getter
@Setter
public class Goods implements Serializable {
    private Integer id;
    private String name;
    private Integer number;
    private Timestamp startTime;
    private Timestamp endTime;
    private Timestamp createTime;
    private BigDecimal price;
}
