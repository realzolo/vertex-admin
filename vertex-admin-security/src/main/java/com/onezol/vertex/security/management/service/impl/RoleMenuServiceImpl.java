package com.onezol.vertex.security.management.service.impl;

import com.onezol.vertex.core.common.service.impl.GenericServiceImpl;
import com.onezol.vertex.security.management.mapper.RoleMenuMapper;
import com.onezol.vertex.security.management.model.entity.RoleMenuEntity;
import com.onezol.vertex.security.management.service.RoleMenuService;
import org.springframework.stereotype.Service;

@Service
public class RoleMenuServiceImpl extends GenericServiceImpl<RoleMenuMapper, RoleMenuEntity> implements RoleMenuService {
}
