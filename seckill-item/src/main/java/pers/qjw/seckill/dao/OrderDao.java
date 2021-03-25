package pers.qjw.seckill.dao;

import org.apache.ibatis.annotations.*;
import pers.qjw.seckill.domain.Order;

import java.util.List;

@Mapper
public interface OrderDao {

    @Insert("INSERT INTO `seckill`.`seckill_order` (`id`, `seckill_goods_id`, `user_id`, `state`, `create_time`) VALUES (#{id}, #{goodsId}, #{userId}, #{state}, #{createTime});")
    int insertOrder(Order order);

    @Select("SELECT `id`,`seckill_goods_id`,`user_id`,`state`,`create_time` FROM seckill.seckill_order where (`id` = #{id});")
    Order getOrder(int id);

    @Update("UPDATE `seckill`.`seckill_order` SET `state` = #{state} WHERE (`id` = #{id});")
    int updateOrder(int id, int state);

    @Select("SELECT `id`,`seckill_goods_id`,`user_id`,`state`,`create_time` FROM seckill.seckill_order where (user_id = #{userId})")
    @Results(
            id = "order",
            value = {
                    @Result(column = "seckill_goods_id", property = "goodsId"),
                    @Result(column = "user_id", property = "userId"),
                    @Result(column = "create_time", property = "createTime")
            }
    )
    List<Order> listOrders(int userId);

}
