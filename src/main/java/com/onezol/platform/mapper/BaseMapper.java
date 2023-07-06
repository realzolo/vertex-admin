package com.onezol.platform.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.onezol.platform.model.entity.BaseEntity;
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
public interface BaseMapper<T extends BaseEntity> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {

    @Delete("DELETE FROM ${tableName} ${ew.customSqlSegment}")
    int opsForDelete(@Param("tableName") String tableName, @Param(Constants.WRAPPER) Wrapper<T> wrapper);

    @Select("SELECT * FROM ${tableName} ${ew.customSqlSegment}")
    T opsForSelect(@Param("tableName") String tableName, @Param(Constants.WRAPPER) Wrapper<T> wrapper);

    @Select("SELECT * FROM ${tableName} ${ew.customSqlSegment}")
    T[] opsForSelectList(@Param("tableName") String tableName, @Param(Constants.WRAPPER) Wrapper<T> wrapper);

}
