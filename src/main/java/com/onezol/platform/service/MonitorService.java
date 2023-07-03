package com.onezol.platform.service;

import com.onezol.platform.model.dto.SystemInfoWrapper;

public interface MonitorService {
    /**
     * 获取当前系统监控信息
     *
     * @return 当前系统监控信息
     */
    SystemInfoWrapper getSystemInfo();

    /**
     * 获取缓存(Redis)信息
     *
     * @return 缓存信息
     */
    Object getCacheInfo();
}
