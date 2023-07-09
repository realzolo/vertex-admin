package com.onezol.vertex.core.service;


import com.onezol.vertex.core.model.dto.UserLoginLog;
import com.onezol.vertex.core.model.entity.UserLoginLogEntity;

public interface UserLoginLogService extends BaseService<UserLoginLogEntity> {
    /**
     * 创建日志
     *
     * @param userLoginLog 日志实体
     */
    void createLog(UserLoginLog userLoginLog);
}
