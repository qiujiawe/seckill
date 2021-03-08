package pers.qjw.seckill.dao;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import pers.qjw.seckill.domain.User;

@Mapper
public interface UserDao {

    @Select("SELECT `id`,`phone`,`password` FROM user WHERE (`phone` = #{phone});")
    User getUser(String phone);

    @Insert("INSERT INTO `user` (`phone`, `password`) VALUES (#{phone}, #{password});")
    int insertUser(User user);

}
