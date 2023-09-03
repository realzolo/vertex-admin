package com.onezol.vertex.security.api.mapper;

import com.onezol.vertex.common.mapper.BaseMapper;
import com.onezol.vertex.security.api.model.entity.UserLoginLogEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserLoginLogMapper extends BaseMapper<UserLoginLogEntity> {
}
