package com.onezol.vertex.security.biz.service;

import com.onezol.vertex.common.service.impl.BaseServiceImpl;
import com.onezol.vertex.security.api.mapper.RoleMenuMapper;
import com.onezol.vertex.security.api.model.entity.RoleMenuEntity;
import com.onezol.vertex.security.api.service.RoleMenuService;
import org.springframework.stereotype.Service;

@Service
public class RoleMenuServiceImpl extends BaseServiceImpl<RoleMenuMapper, RoleMenuEntity> implements RoleMenuService {
}
