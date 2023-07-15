package com.onezol.vertex.app.controller;

import com.onezol.vertex.common.annotation.Loggable;
import com.onezol.vertex.common.model.payload.GenericPayload;
import com.onezol.vertex.common.model.record.ListResultWrapper;
import com.onezol.vertex.core.module.log.model.dto.AccessLog;
import com.onezol.vertex.core.module.log.model.entity.AccessLogEntity;
import com.onezol.vertex.core.module.log.service.AccessLogService;
import com.onezol.vertex.core.module.monitor.model.dto.SystemInfoWrapper;
import com.onezol.vertex.core.module.monitor.service.MonitorService;
import com.onezol.vertex.core.util.ModelUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "监控管理")
@Loggable
@RestController
@RequestMapping("/monitor")
public class MonitorController {

    private final MonitorService monitorService;
    private final AccessLogService accessLogService;

    public MonitorController(MonitorService monitorService, AccessLogService accessLogService) {
        this.monitorService = monitorService;
        this.accessLogService = accessLogService;
    }

    @Operation(summary = "查询系统信息", description = "查询系统信息")
    @GetMapping("/server")
    public SystemInfoWrapper getSystemInfo() {
        return monitorService.getSystemInfo();
    }


    @Operation(summary = "查询缓存信息", description = "查询缓存信息")
    @GetMapping("/cache")
    public Object getCacheInfo() {
        return monitorService.getCacheInfo();
    }

    @Operation(summary = "查询访问日志", description = "查询访问日志列表", hidden = true)
    @PostMapping("/access-log")
    public Object getRequestLog(@RequestBody GenericPayload payload) {
        ListResultWrapper<AccessLogEntity> resultWrapper = accessLogService.queryList(payload);
        return ModelUtils.convert(resultWrapper, AccessLog.class);
    }

    @Operation(summary = "查询访问日志", description = "查询访问日志详情", hidden = true)
    @GetMapping("/access-log/{id}")
    public AccessLog getRequestLog(@PathVariable("id") Long id) {
        AccessLogEntity entity = accessLogService.getById(id);
        return ModelUtils.convert(entity, AccessLog.class);
    }
}
