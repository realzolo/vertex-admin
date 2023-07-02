package com.onezol.platform.service.impl;

import com.onezol.platform.mapper.UserLoginLogMapper;
import com.onezol.platform.model.dto.UserLoginLog;
import com.onezol.platform.model.entity.UserLoginLogEntity;
import com.onezol.platform.service.UserLoginLogService;
import com.onezol.platform.util.ConvertUtils;
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
        UserLoginLogEntity userLoginLogEntity = ConvertUtils.convertTo(userLoginLog, UserLoginLogEntity.class);
        this.save(userLoginLogEntity);
    }
}
