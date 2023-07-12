package com.onezol.vertex.security.management.service.impl;

import com.onezol.vertex.core.common.service.impl.BaseServiceImpl;
import com.onezol.vertex.security.management.mapper.UserRoleMapper;
import com.onezol.vertex.security.management.model.entity.UserRoleEntity;
import com.onezol.vertex.security.management.service.UserRoleService;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRoleMapper, UserRoleEntity> implements UserRoleService {
}
