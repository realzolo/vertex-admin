package com.onezol.vertex.app.controller;

import com.onezol.vertex.core.module.monitor.model.dto.SystemInfoWrapper;
import com.onezol.vertex.core.module.monitor.service.MonitorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monitor")
public class MonitorController {

    private final MonitorService monitorService;

    public MonitorController(MonitorService monitorService) {
        this.monitorService = monitorService;
    }

    @GetMapping("/server")
    public SystemInfoWrapper getSystemInfo() {
        return monitorService.getSystemInfo();
    }


    @GetMapping("/cache")
    public Object getCacheInfo() {
        return monitorService.getCacheInfo();
    }
}
