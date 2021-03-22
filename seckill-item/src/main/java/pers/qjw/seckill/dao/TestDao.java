package pers.qjw.seckill.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestDao {

    @Insert("INSERT INTO `seckill`.`test` (`test`, `test2`, `test3`) VALUES ('1', '1', '1');")
    void insert();
}
