package com.funnycode.hyjal.db.core;

import com.funnycode.hyjal.db.annotation.HyjalDataSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author tc
 * @date 2019-03-24
 */
@Mapper
@HyjalDataSource(name = "local-db")
public interface TestDb1Mapper {

    @Select("select app_name from malan_app WHERE app_id = #{id}")
    @HyjalDataSource(name = "local-db")
    String getName(@Param("id") long id);

}
