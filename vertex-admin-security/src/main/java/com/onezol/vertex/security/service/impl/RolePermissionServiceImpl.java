package com.onezol.vertex.security.service.impl;

import com.onezol.vertex.core.service.impl.GenericServiceImpl;
import com.onezol.vertex.security.mapper.RolePermissionMapper;
import com.onezol.vertex.security.model.entity.RolePermissionEntity;
import com.onezol.vertex.security.service.RolePermissionService;
import org.springframework.stereotype.Service;

@Service
public class RolePermissionServiceImpl extends GenericServiceImpl<RolePermissionMapper, RolePermissionEntity> implements RolePermissionService {
}
