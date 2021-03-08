package pers.qjw.seckill.dao;

import org.apache.ibatis.annotations.*;
import pers.qjw.seckill.domain.Goods;

import java.util.List;

@Mapper
public interface GoodsDao {

    @Select("SELECT `id`,`name`,`number`,`start_time`,`end_time`,`create_time`,`price` FROM seckill_goods;")
    @Results(
            id = "goods",
            value = {
                    @Result(property = "startTime", column = "start_time"),
                    @Result(property = "endTime", column = "end_time"),
                    @Result(property = "createTime", column = "create_time")
            }
    )
    List<Goods> listGoods();

    @Select("SELECT `id`,`name`,`number`,`start_time`,`end_time`,`create_time`,`price` FROM seckill_goods where id = #{id};")
    @ResultMap("goods")
    Goods getGoods(Integer id);

    @Update("UPDATE `seckill`.`seckill_goods` SET `name` = #{name}, `number` = #{number}, `start_time` = #{startTime}, `end_time` = #{endTime}, `create_time` = #{createTime}, `price` = #{price} WHERE (`id` = #{id});")
    int updateGoods(Goods goods);

}
