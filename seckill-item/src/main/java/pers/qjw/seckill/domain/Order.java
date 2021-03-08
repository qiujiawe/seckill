package pers.qjw.seckill.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Order {
    private Integer id;
    // 0 未支付 1 已支付 -1 订单已超时(长时间未支付)
    private Integer state;
    private Timestamp createTime;
    private Integer goodsId;
    private Integer userId;
}
