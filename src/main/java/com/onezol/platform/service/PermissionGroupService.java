package com.onezol.platform.service;

import com.onezol.platform.model.dto.PermissionGroup;
import com.onezol.platform.model.entity.PermissionGroupEntity;

public interface PermissionGroupService extends BaseService<PermissionGroupEntity> {
    /**
     * 创建权限组
     *
     * @param permissionGroup        权限组
     * @param autoGeneratePermission 是否自动生成权限
     * @return 权限组
     */
    PermissionGroup createPermissionGroup(PermissionGroup permissionGroup, boolean autoGeneratePermission);
}
