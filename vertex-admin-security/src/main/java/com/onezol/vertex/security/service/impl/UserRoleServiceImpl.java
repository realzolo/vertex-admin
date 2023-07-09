package com.onezol.vertex.security.service.impl;

import com.onezol.vertex.core.service.impl.BaseServiceImpl;
import com.onezol.vertex.security.mapper.UserRoleMapper;
import com.onezol.vertex.security.model.entity.UserRoleEntity;
import com.onezol.vertex.security.service.UserRoleService;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRoleMapper, UserRoleEntity> implements UserRoleService {
}
