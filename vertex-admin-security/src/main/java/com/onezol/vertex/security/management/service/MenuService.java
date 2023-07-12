package com.onezol.vertex.security.management.service;

import com.onezol.vertex.core.common.service.GenericService;
import com.onezol.vertex.security.management.model.entity.MenuEntity;

import java.util.Set;

public interface MenuService extends GenericService<MenuEntity> {
    /**
     * 根据用户id获取权限列表
     *
     * @param userId 用户id
     * @return permissions
     */
    Set<String> getPermsByUserId(Long userId);
}
