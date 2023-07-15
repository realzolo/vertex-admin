package com.onezol.vertex.core.security.management.service;

import com.onezol.vertex.core.base.service.impl.BaseServiceImpl;
import com.onezol.vertex.core.security.management.mapper.UserLoginLogMapper;
import com.onezol.vertex.core.security.management.model.dto.UserLoginLog;
import com.onezol.vertex.core.security.management.model.entity.UserLoginLogEntity;
import com.onezol.vertex.core.util.ModelUtils;
import org.springframework.stereotype.Service;

@Service
public class UserLoginLogService extends BaseServiceImpl<UserLoginLogMapper, UserLoginLogEntity> {
    /**
     * 创建日志
     *
     * @param userLoginLog 日志实体
     */
    public void createLog(UserLoginLog userLoginLog) {
        if (userLoginLog == null) {
            return;
        }
        UserLoginLogEntity userLoginLogEntity = ModelUtils.convert(userLoginLog, UserLoginLogEntity.class);
        this.save(userLoginLogEntity);
    }
}
