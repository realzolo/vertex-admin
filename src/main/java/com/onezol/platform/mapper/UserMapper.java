package com.onezol.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.onezol.platform.model.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}
