package com.onezol.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onezol.platform.mapper.UserMapper;
import com.onezol.platform.model.entity.UserEntity;
import com.onezol.platform.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {
}
