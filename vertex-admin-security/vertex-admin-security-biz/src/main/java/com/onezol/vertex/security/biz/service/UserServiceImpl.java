package com.onezol.vertex.security.biz.service;

import com.onezol.vertex.common.model.payload.GenericPayload;
import com.onezol.vertex.common.model.record.ListResultWrapper;
import com.onezol.vertex.common.service.impl.BaseServiceImpl;
import com.onezol.vertex.security.api.mapper.UserMapper;
import com.onezol.vertex.security.api.model.entity.UserEntity;
import com.onezol.vertex.security.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, UserEntity> implements UserService {
    /**
     * 根据id查询
     *
     * @param id 主键
     * @return 实体
     */
    @Override
    public UserEntity queryOne(Long id) {
        return null;
    }

    /**
     * 条件查询
     *
     * @param payload 查询参数
     */
    @Override
    public ListResultWrapper<UserEntity> queryList(GenericPayload payload) {
        return null;
    }

    /**
     * 删除
     *
     * @param id             ID
     * @param physicalDelete 是否物理删除
     */
    @Override
    public void delete(Long id, boolean physicalDelete) {

    }

    /**
     * 删除
     *
     * @param ids            ID列表
     * @param physicalDelete 是否物理删除
     */
    @Override
    public void delete(Long[] ids, boolean physicalDelete) {

    }

    /**
     * 保存/更新
     *
     * @param data 数据
     * @return 保存/更新结果
     */
    @Override
    public UserEntity save(Map<String, Object> data) {
        return null;
    }
}
