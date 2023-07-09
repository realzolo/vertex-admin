package com.onezol.vertex.app.controller;

import com.onezol.vertex.common.model.BaseDTO;
import com.onezol.vertex.common.model.BaseEntity;
import com.onezol.vertex.common.model.BaseParam;
import com.onezol.vertex.common.pojo.ListResultWrapper;
import com.onezol.vertex.core.annotation.ControllerService;
import com.onezol.vertex.core.model.param.DeleteParam;
import com.onezol.vertex.core.model.param.GenericParam;
import com.onezol.vertex.core.service.GenericService;
import com.onezol.vertex.core.util.ModelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@SuppressWarnings("unchecked")
public class GenericController<T extends BaseEntity, P extends BaseParam> {
    private final Logger logger = LoggerFactory.getLogger(GenericController.class);
    private GenericService<T> service;
    private Class<? extends BaseDTO> clazz;

    @Autowired
    public final void setService(ApplicationContext context) {
        Class<?> controllerClass = this.getClass();
        ControllerService annotation = controllerClass.getAnnotation(ControllerService.class);
        if (annotation == null) {
            throw new RuntimeException("在 [" + controllerClass.getName() + "] 上未使用 [ControllerService] 注解, 无法启用通用接口服务");
        }
        Class<?> serviceClass = annotation.service();
        this.service = (GenericService<T>) context.getBean(serviceClass);
        this.clazz = annotation.retClass();
    }

    /**
     * 查询: /{controllerName}/{id}
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public BaseDTO getById(@PathVariable Long id) {
        T entity = service.getById(id);
        return ModelUtils.convert(entity, clazz);
    }

    /**
     * 查询列表： /{controllerName}/list
     *
     * @param param 通用参数
     * @return 结果列表
     */
    @PostMapping("/list")
    public ListResultWrapper<? extends BaseDTO> list(@RequestBody @Validated GenericParam param) {
        ListResultWrapper<T> resultWrapper = service.queryList(param);
        return ModelUtils.convert(resultWrapper, clazz);
    }

    /**
     * 保存/更新： /{controllerName}/save
     *
     * @param param 通用参数
     * @return 保存/更新后的实体
     */
    @PostMapping("/save")
    public BaseDTO save(@RequestBody P param) {
        T entity = service.save(param);
        return ModelUtils.convert(entity, clazz);
    }

    /**
     * 删除： /{controllerName}/delete
     *
     * @param param 删除参数
     */
    @DeleteMapping("/delete")
    public void delete(@RequestBody @Validated DeleteParam param) {
        Long[] ids = param.getIds();
        boolean physicalDelete = !Objects.isNull(param.getPhysicalDelete()) && param.getPhysicalDelete();
        service.delete(ids, physicalDelete);
    }
}
