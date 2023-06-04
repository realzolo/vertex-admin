package com.onezol.platform.service;

import com.onezol.platform.model.entity.BaseEntity;

/**
 * 基础服务接口
 *
 * @param <T> BaseEntity的子类
 */
public interface BaseService<T extends BaseEntity> extends com.baomidou.mybatisplus.extension.service.IService<T> {
}
