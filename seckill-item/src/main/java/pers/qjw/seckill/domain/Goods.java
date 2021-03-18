package pers.qjw.seckill.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class Goods implements Serializable {
    private Integer id;
    private String name;
    private Integer number;
    private Timestamp startTime;
    private Timestamp endTime;
    private Timestamp createTime;
    private BigDecimal price;
}
