package com.onezol.vertex.core.module.log.service;

import com.onezol.vertex.common.service.impl.BaseServiceImpl;
import com.onezol.vertex.core.module.log.mapper.AccessLogMapper;
import com.onezol.vertex.core.module.log.model.entity.AccessLogEntity;
import org.springframework.stereotype.Service;

@Service
public class AccessLogService extends BaseServiceImpl<AccessLogMapper, AccessLogEntity> {
}
