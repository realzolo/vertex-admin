package com.onezol.vertex.security.management.service;

import com.onezol.vertex.core.common.service.BaseService;
import com.onezol.vertex.security.management.model.dto.UserLoginLog;
import com.onezol.vertex.security.management.model.entity.UserLoginLogEntity;

public interface UserLoginLogService extends BaseService<UserLoginLogEntity> {
    /**
     * 创建日志
     *
     * @param userLoginLog 日志实体
     */
    void createLog(UserLoginLog userLoginLog);
}
