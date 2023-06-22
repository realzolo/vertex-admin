package com.onezol.platform.service.impl;

import com.onezol.platform.mapper.UserRoleMapper;
import com.onezol.platform.model.entity.UserRoleEntity;
import com.onezol.platform.service.UserRoleService;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRoleMapper, UserRoleEntity> implements UserRoleService {
}
