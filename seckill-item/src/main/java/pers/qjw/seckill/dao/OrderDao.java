package pers.qjw.seckill.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import pers.qjw.seckill.domain.Order;

@Mapper
public interface OrderDao {

    @Insert("INSERT INTO `seckill`.`seckill_order` (`seckill_goods_id`, `user_id`, `state`, `create_time`) VALUES (#{goodsId}, #{userId}, #{state}, #{createTime});")
    int insert(Order order);
}
