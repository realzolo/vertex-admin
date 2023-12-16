package com.onezol.vertex.core.module.log.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.onezol.vertex.common.annotation.RestResponse;
import com.onezol.vertex.common.controller.BaseController;
import com.onezol.vertex.common.model.record.PlainPage;
import com.onezol.vertex.common.util.ObjectConverter;
import com.onezol.vertex.core.module.log.model.dto.AccessLog;
import com.onezol.vertex.core.module.log.model.entity.AccessLogEntity;
import com.onezol.vertex.core.module.log.service.AccessLogService;
import com.onezol.vertex.security.api.annotation.RestrictAccess;
import com.onezol.vertex.security.api.context.UserContextHolder;
import com.onezol.vertex.security.api.model.dto.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Tag(name = "访问日志")
@RestrictAccess
@RestController
@RestResponse
@RequestMapping("/access-log")
public class AccessLogController extends BaseController<AccessLogEntity> {
    private final AccessLogService accessLogService;

    public AccessLogController(AccessLogService accessLogService) {
        this.accessLogService = accessLogService;
    }

    @Operation(summary = "获取访问日志列表")
    @GetMapping("/list")
    public PlainPage<AccessLog> list(
            @RequestParam(value = "pageNo", required = false) Long pageNo,
            @RequestParam(value = "pageSize", required = false) Long pageSize,
            @RequestParam(value = "all", defaultValue = "false") Boolean showAll,
            @RequestParam(value = "userId", required = false) Long userId
    ) {
        IPage<AccessLogEntity> page = getPage(pageNo, pageSize);
        if (showAll) {
            userId = null;
        }
        if (!showAll && Objects.isNull(userId)){
            User user = UserContextHolder.getContext().getUserDetails();
            userId = user.getId();
        }

        return accessLogService.list(page, userId);
    }



    @Operation(summary = "获取日志详情", description = "获取日志详情", hidden = true)
    @GetMapping("/access-log")
    public AccessLog getRequestLog(@RequestParam("id") Long id) {
        AccessLogEntity entity = accessLogService.getById(id);
        return ObjectConverter.convert(entity, AccessLog.class);
    }
}
