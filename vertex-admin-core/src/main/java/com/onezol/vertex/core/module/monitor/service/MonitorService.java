package com.onezol.vertex.core.module.monitor.service;


import com.onezol.vertex.core.module.monitor.model.dto.SystemInfoWrapper;

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
