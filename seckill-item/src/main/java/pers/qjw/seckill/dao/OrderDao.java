package pers.qjw.seckill.dao;

import org.apache.ibatis.annotations.*;
import pers.qjw.seckill.domain.Order;

import java.util.List;

@Mapper
public interface OrderDao {

    @Insert("INSERT INTO `seckill`.`seckill_order` (`seckill_goods_id`, `user_id`, `state`, `create_time`) VALUES (#{goodsId}, #{userId}, #{state}, #{createTime});")
    int insertOrder(Order order);

    @Select("SELECT id,seckill_goods_id,user_id,state,create_time FROM seckill.seckill_order where user_id = #{userId};")
    @Results({
            @Result(property = "goodsId", column = "seckill_goods_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "createTime", column = "create_time")
    })
    List<Order> listOrders(int userId);

    @Update("UPDATE `seckill`.`seckill_order` SET `state` = #{state} WHERE (`user_id` = #{userId}) and (`seckill_goods_id` = #{goodsId});")
    int updateOrder(int userId, int goodsId, int state);
}
