package com.onezol.platform.mapper;

import com.onezol.platform.model.entity.BaseEntity;
import org.apache.ibatis.annotations.*;

/**
 * 基础Mapper
 *
 * @param <T> BaseEntity的子类
 */
@Mapper
public interface BaseMapper<T extends BaseEntity> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {

    @Insert("${SQL}")
    void opsForInsert(@Param("SQL") String sql);

    @Delete("${SQL}")
    int opsForDelete(@Param("SQL") String sql);

    @Update("${SQL}")
    int opsForUpdate(@Param("SQL") String sql);

    @Select("${SQL}")
    T opsForSelect(@Param("SQL") String sql);

    @Select("${SQL}")
    T[] opsForSelectList(@Param("SQL") String sql);
}
