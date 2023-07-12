package com.onezol.vertex.security.management.mapper;

import com.onezol.vertex.core.common.mapper.BaseMapper;
import com.onezol.vertex.security.management.model.entity.MenuEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

@Mapper
public interface MenuMapper extends BaseMapper<MenuEntity> {
    /**
     * 根据用户ID查询权限列表
     */
    Set<String> selectPermsByUserId(Long userId);
}
