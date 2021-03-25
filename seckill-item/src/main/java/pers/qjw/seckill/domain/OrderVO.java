package pers.qjw.seckill.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
public class OrderVO implements Serializable {
    private String goodsName;
    private Integer id;
    private Integer state;
    private Timestamp createTime;

    public OrderVO(Order order,String goodsName){
        this.goodsName = goodsName;
        this.id = order.getId();
        this.state = order.getState();
        this.createTime = order.getCreateTime();
    }
}
