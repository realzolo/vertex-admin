package com.onezol.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onezol.platform.mapper.BaseMapper;
import com.onezol.platform.model.entity.BaseEntity;
import com.onezol.platform.service.BaseService;

/**
 * 基础服务实现类
 *
 * @param <M> BaseMapper的子类
 * @param <T> BaseEntity的子类
 */
public abstract class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity>
        extends ServiceImpl<M, T> implements BaseService<T> {
}
