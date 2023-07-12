package com.onezol.vertex.security.management.mapper;

import com.onezol.vertex.core.common.mapper.BaseMapper;
import com.onezol.vertex.security.management.model.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}
