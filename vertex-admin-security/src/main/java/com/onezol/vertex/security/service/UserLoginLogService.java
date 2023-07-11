package com.onezol.vertex.security.service;

import com.onezol.vertex.core.common.service.BaseService;
import com.onezol.vertex.security.model.dto.UserLoginLog;
import com.onezol.vertex.security.model.entity.UserLoginLogEntity;

public interface UserLoginLogService extends BaseService<UserLoginLogEntity> {
    /**
     * 创建日志
     *
     * @param userLoginLog 日志实体
     */
    void createLog(UserLoginLog userLoginLog);
}
