package com.onezol.vertex.security.management.service;

import com.onezol.vertex.core.common.service.GenericService;
import com.onezol.vertex.security.management.model.dto.PermissionGroup;
import com.onezol.vertex.security.management.model.entity.PermissionGroupEntity;

public interface PermissionGroupService extends GenericService<PermissionGroupEntity> {
    /**
     * 创建权限组
     *
     * @param permissionGroup        权限组
     * @param autoGeneratePermission 是否自动生成权限
     * @return 权限组
     */
    PermissionGroup createPermissionGroup(PermissionGroup permissionGroup, boolean autoGeneratePermission);
}
