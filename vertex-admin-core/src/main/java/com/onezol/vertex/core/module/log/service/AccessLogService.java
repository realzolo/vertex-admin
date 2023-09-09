package com.onezol.vertex.core.module.log.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.common.model.record.PlainPage;
import com.onezol.vertex.common.service.impl.BaseServiceImpl;
import com.onezol.vertex.core.module.log.mapper.AccessLogMapper;
import com.onezol.vertex.core.module.log.model.dto.AccessLog;
import com.onezol.vertex.core.module.log.model.entity.AccessLogEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AccessLogService extends BaseServiceImpl<AccessLogMapper, AccessLogEntity> {
    /**
     * 获取用户的访问日志
     *
     * @param page   分页对象
     * @param userId 用户ID
     * @return 访问日志
     */
    public PlainPage<AccessLog> list(IPage<AccessLogEntity> page, Long userId) {
        Wrapper<AccessLogEntity> wrapper = Wrappers.<AccessLogEntity>lambdaQuery()
                .eq(Objects.nonNull(userId), AccessLogEntity::getUserId, userId)
                .orderByDesc(AccessLogEntity::getCreatedAt);
        IPage<AccessLogEntity> pageResult = this.page(page, wrapper);
        return PlainPage.from(pageResult, AccessLog.class);
    }
}
