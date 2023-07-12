package com.onezol.vertex.security.management.controller;

import com.onezol.vertex.common.model.dto.DTO;
import com.onezol.vertex.common.model.payload.GenericPayload;
import com.onezol.vertex.common.model.record.ListResultWrapper;
import com.onezol.vertex.core.common.controller.GenericController;
import com.onezol.vertex.core.util.ModelUtils;
import com.onezol.vertex.security.management.model.dto.PermissionGroup;
import com.onezol.vertex.security.management.model.payload.PermissionGroupPayload;
import com.onezol.vertex.security.management.service.PermissionGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/permission-group")
public class PermissionGroupController extends GenericController<PermissionGroupService> {

    @Autowired
    private PermissionGroupService permissionGroupService;

    /**
     * 获取DTO的Class(返回给前端的数据类型, 避免Entity直接暴露给前端)
     *
     * @return DTO Class
     */
    @Override
    protected Class<? extends DTO> getDtoClass() {
        return PermissionGroup.class;
    }

    /**
     * 查询列表： /xxx/list
     */
    @Override
    @PostMapping("/list")
    protected ListResultWrapper<? extends DTO> queryList(@RequestBody GenericPayload payload) {
        return super.queryList(payload);
    }

    /**
     * 保存/更新： /xxx/save
     */
    public DTO save(@RequestBody PermissionGroupPayload payload) {
        boolean autoGeneratePermission = !Objects.isNull(payload.getAutoGeneratePermission()) && payload.getAutoGeneratePermission();
        PermissionGroup permissionGroup = ModelUtils.convert(payload, PermissionGroup.class);
        return permissionGroupService.createPermissionGroup(permissionGroup, autoGeneratePermission);
    }

    /**
     * 删除： /xxx/delete
     */
    @Override
    @PostMapping("/delete")
    protected void delete(@RequestBody GenericPayload payload) {
        super.delete(payload);
    }
}
