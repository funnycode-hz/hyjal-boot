package com.funnycode.hyjal.db.core;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author tc
 * @date 2019-03-24
 */
@Mapper
public interface TestDb2Mapper {

    @Select("select user_id from account_tbl WHERE id = #{id}")
    String getName(@Param("id") long id);

}
