package com.onezol.vertex.core.module.log.mapper;

import com.onezol.vertex.common.mapper.BaseMapper;
import com.onezol.vertex.core.module.log.model.entity.AccessLogEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccessLogMapper extends BaseMapper<AccessLogEntity> {
}
