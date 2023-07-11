package com.onezol.vertex.core.common.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.onezol.vertex.core.common.model.entity.Entity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 基础Mapper
 *
 * @param <T> BaseEntity的子类
 */
@Mapper
public interface BaseMapper<T extends Entity> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {

    @Select("SELECT COUNT(*) FROM ${tableName} ${ew.customSqlSegment}")
    int opsForCount(@Param("tableName") String tableName, @Param(Constants.WRAPPER) Wrapper<T> wrapper);

    @Delete("DELETE FROM ${tableName} ${ew.customSqlSegment}")
    int opsForDelete(@Param("tableName") String tableName, @Param(Constants.WRAPPER) Wrapper<T> wrapper);

    @Select("SELECT * FROM ${tableName} ${ew.customSqlSegment}")
    T opsForSelect(@Param("tableName") String tableName, @Param(Constants.WRAPPER) Wrapper<T> wrapper);

    @Select("SELECT * FROM ${tableName} ${ew.customSqlSegment}")
    T[] opsForSelectList(@Param("tableName") String tableName, @Param(Constants.WRAPPER) Wrapper<T> wrapper);

}
