package pers.qjw.seckill.dao;

import org.apache.ibatis.annotations.*;
import pers.qjw.seckill.domain.Goods;

import java.util.List;

@Mapper
public interface GoodsDao {
    @Select("SELECT `id`,`name`,`number`,`start_time`,`end_time`,`create_time`,`price` FROM seckill.seckill_goods limit 0,10;")
    @Results(
        id = "goods",
        value = {
            @Result(column = "start_time", property = "startTime"),
            @Result(column = "end_time", property = "endTime"),
            @Result(column = "create_time", property = "createTime")
        }
    )
    List<Goods> getHotCommodity();

    @ResultMap("goods")
    @Select("SELECT `id`,`name`,`number`,`start_time`,`end_time`,`create_time`,`price` FROM seckill.seckill_goods WHERE (`id` = #{id})")
    Goods getGoods(int id);

    @Insert("UPDATE `seckill`.`seckill_goods` SET `number` = #{inventory} WHERE (`id` = #{goodsId});")
    int setInventory(int goodsId, int inventory);
}
