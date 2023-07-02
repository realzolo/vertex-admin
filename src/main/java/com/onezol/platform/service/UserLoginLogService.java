package com.onezol.platform.service;

import com.onezol.platform.model.dto.UserLoginLog;
import com.onezol.platform.model.entity.UserLoginLogEntity;

public interface UserLoginLogService extends BaseService<UserLoginLogEntity> {
    /**
     * 创建日志
     *
     * @param userLoginLog 日志实体
     */
    void createLog(UserLoginLog userLoginLog);
}
