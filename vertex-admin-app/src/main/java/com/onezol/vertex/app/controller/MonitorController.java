package com.onezol.vertex.app.controller;

import com.onezol.vertex.core.model.dto.SystemInfoWrapper;
import com.onezol.vertex.core.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monitor")
public class MonitorController {

    @Autowired
    private MonitorService monitorService;

    @GetMapping("/server")
    public SystemInfoWrapper getSystemInfo() {
        return monitorService.getSystemInfo();
    }


    @GetMapping("/cache")
    public Object getCacheInfo() {
        return monitorService.getCacheInfo();
    }
}
