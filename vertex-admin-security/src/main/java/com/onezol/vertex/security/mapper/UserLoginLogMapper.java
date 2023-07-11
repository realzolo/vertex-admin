package com.onezol.vertex.security.mapper;

import com.onezol.vertex.core.common.mapper.BaseMapper;
import com.onezol.vertex.security.model.entity.UserLoginLogEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserLoginLogMapper extends BaseMapper<UserLoginLogEntity> {
}
