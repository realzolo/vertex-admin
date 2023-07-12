package com.onezol.vertex.security.management.service.impl;

import com.onezol.vertex.core.common.service.impl.GenericServiceImpl;
import com.onezol.vertex.security.management.mapper.RolePermissionMapper;
import com.onezol.vertex.security.management.model.entity.RolePermissionEntity;
import com.onezol.vertex.security.management.service.RolePermissionService;
import org.springframework.stereotype.Service;

@Service
public class RolePermissionServiceImpl extends GenericServiceImpl<RolePermissionMapper, RolePermissionEntity> implements RolePermissionService {
}
