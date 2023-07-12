package com.onezol.vertex.core.common.controller;

import com.onezol.vertex.common.model.dto.DTO;
import com.onezol.vertex.common.model.entity.Entity;
import com.onezol.vertex.common.model.payload.GenericPayload;
import com.onezol.vertex.common.model.record.ListResultWrapper;
import com.onezol.vertex.core.common.service.GenericService;
import com.onezol.vertex.core.util.ModelUtils;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

/**
 * 通用控制器, 简化增删改查操作
 */
public abstract class GenericController<S extends GenericService<? extends Entity>> {
    private final Class<? extends DTO> dtoClass = getDtoClass();
    private S service;

    @Resource
    public void setService(S service) {
        this.service = service;
    }

    /**
     * 查询: /xxx/query
     */
    protected DTO queryById(GenericPayload payload) {
        Long id = payload.getId();
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("id can not be null");
        }
        Entity entity = service.getById(id);
        return ModelUtils.convert(entity, dtoClass);
    }

    /**
     * 查询列表： /xxx/list
     */
    protected ListResultWrapper<? extends DTO> queryList(GenericPayload payload) {
        ListResultWrapper<? extends Entity> resultWrapper = service.queryList(payload);
        return ModelUtils.convert(resultWrapper, dtoClass);
    }

    /**
     * 保存/更新： /xxx/save
     */
    protected DTO save(GenericPayload payload) {
        Map<String, Object> data = payload.getData();
        if (Objects.isNull(data) || data.isEmpty()) {
            throw new IllegalArgumentException("data can not be null");
        }
        Entity entity = service.save(data);
        return ModelUtils.convert(entity, dtoClass);
    }

    /**
     * 删除： /xxx/delete
     */
    protected void delete(GenericPayload payload) {
        Long[] ids = payload.getIds();
        if (Objects.isNull(ids) || ids.length == 0) {
            return;
        }
        boolean physicalDelete = !Objects.isNull(payload.getPhysicalDelete()) && payload.getPhysicalDelete();
        service.delete(ids, physicalDelete);
    }

    /**
     * 获取DTO的Class(返回给前端的数据类型, 避免Entity直接暴露给前端)
     *
     * @return DTO Class
     */
    protected abstract Class<? extends DTO> getDtoClass();
}
