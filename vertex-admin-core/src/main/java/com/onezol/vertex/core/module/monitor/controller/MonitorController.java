package com.onezol.vertex.core.module.monitor.controller;

import com.onezol.vertex.common.annotation.Loggable;
import com.onezol.vertex.core.module.monitor.model.dto.SystemInfoWrapper;
import com.onezol.vertex.core.module.monitor.service.MonitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "监控管理")
@Loggable
@RestController
@RequestMapping("/monitor")
public class MonitorController {

    private final MonitorService monitorService;

    public MonitorController(MonitorService monitorService) {
        this.monitorService = monitorService;
    }

    @Operation(summary = "获取系统信息", description = "获取系统信息")
    @GetMapping("/server")
    public SystemInfoWrapper getSystemInfo() {
        return monitorService.getSystemInfo();
    }

    @Operation(summary = "获取缓存信息", description = "获取Redis缓存信息")
    @GetMapping("/redis")
    public Object getCacheInfo() {
        return monitorService.getCacheInfo();
    }
}