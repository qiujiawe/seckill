package pers.qjw.seckill.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import pers.qjw.seckill.domain.User;

/**
 * UserDao类是提供给ibatis做动态代理的
 */
@Mapper
public interface UserDao {

    /**
     * 查找数据库中与参数phone一致的字段
     * @param phone 电话号码
     * @return 返回user对象
     */
    @Select("SELECT `id`,`phone`,`password` FROM seckill.user WHERE `phone` = #{phone};")
    User getUser(String phone);

    @Insert("INSERT INTO `seckill`.`user` (`phone`, `password`) VALUES (#{phone}, #{password});")
    int insertUser(String phone, String password);
}
