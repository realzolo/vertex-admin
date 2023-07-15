package com.onezol.vertex.core.security.management.mapper;

import com.onezol.vertex.core.base.mapper.BaseMapper;
import com.onezol.vertex.core.security.management.model.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}
