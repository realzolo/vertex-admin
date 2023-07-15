package com.onezol.vertex.core.module.log.service;

import com.onezol.vertex.core.base.service.impl.GenericServiceImpl;
import com.onezol.vertex.core.module.log.mapper.AccessLogMapper;
import com.onezol.vertex.core.module.log.model.entity.AccessLogEntity;
import org.springframework.stereotype.Service;

@Service
public class AccessLogService extends GenericServiceImpl<AccessLogMapper, AccessLogEntity> {
}
