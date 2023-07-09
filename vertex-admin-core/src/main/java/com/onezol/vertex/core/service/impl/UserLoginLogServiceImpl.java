package com.onezol.vertex.core.service.impl;

import com.onezol.vertex.core.mapper.UserLoginLogMapper;
import com.onezol.vertex.core.model.dto.UserLoginLog;
import com.onezol.vertex.core.model.entity.UserLoginLogEntity;
import com.onezol.vertex.core.service.UserLoginLogService;
import com.onezol.vertex.core.util.ModelUtils;
import org.springframework.stereotype.Service;

@Service
public class UserLoginLogServiceImpl extends BaseServiceImpl<UserLoginLogMapper, UserLoginLogEntity> implements UserLoginLogService {
    /**
     * 创建日志
     *
     * @param userLoginLog 日志实体
     */
    @Override
    public void createLog(UserLoginLog userLoginLog) {
        if (userLoginLog == null) {
            return;
        }
        UserLoginLogEntity userLoginLogEntity = ModelUtils.convert(userLoginLog, UserLoginLogEntity.class);
        this.save(userLoginLogEntity);
    }
}
