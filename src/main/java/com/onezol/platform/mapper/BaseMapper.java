package com.onezol.platform.mapper;

import com.onezol.platform.model.entity.BaseEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 基础Mapper
 *
 * @param <T> BaseEntity的子类
 */
@Mapper
public interface BaseMapper<T extends BaseEntity> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {
}
