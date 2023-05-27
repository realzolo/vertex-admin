package com.onezol.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onezol.app.mapper.UserMapper;
import com.onezol.app.model.entity.UserEntity;
import com.onezol.app.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {
}
