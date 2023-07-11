package com.onezol.vertex.core.common.controller;

import com.onezol.vertex.common.pojo.ListResultWrapper;
import com.onezol.vertex.core.common.model.dto.DTO;
import com.onezol.vertex.core.common.model.entity.Entity;
import com.onezol.vertex.core.common.model.param.GenericParam;
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
    protected DTO queryById(GenericParam param) {
        Long id = param.getId();
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("id can not be null");
        }
        Entity entity = service.getById(id);
        return ModelUtils.convert(entity, dtoClass);
    }

    /**
     * 查询列表： /xxx/list
     */
    protected ListResultWrapper<? extends DTO> queryList(GenericParam param) {
        ListResultWrapper<? extends Entity> resultWrapper = service.queryList(param);
        return ModelUtils.convert(resultWrapper, dtoClass);
    }

    /**
     * 保存/更新： /xxx/save
     */
    protected DTO save(GenericParam param) {
        Map<String, Object> data = param.getData();
        if (Objects.isNull(data) || data.isEmpty()) {
            throw new IllegalArgumentException("data can not be null");
        }
        Entity entity = service.save(data);
        return ModelUtils.convert(entity, dtoClass);
    }

    /**
     * 删除： /xxx/delete
     */
    protected void delete(GenericParam param) {
        Long[] ids = param.getIds();
        if (Objects.isNull(ids) || ids.length == 0) {
            return;
        }
        boolean physicalDelete = !Objects.isNull(param.getPhysicalDelete()) && param.getPhysicalDelete();
        service.delete(ids, physicalDelete);
    }

    /**
     * 获取DTO的Class(返回给前端的数据类型, 避免Entity直接暴露给前端)
     *
     * @return DTO Class
     */
    protected abstract Class<? extends DTO> getDtoClass();
}
