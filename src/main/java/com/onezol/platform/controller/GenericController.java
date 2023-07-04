package com.onezol.platform.controller;

import com.onezol.platform.annotation.ControllerService;
import com.onezol.platform.annotation.Validated;
import com.onezol.platform.model.dto.BaseDTO;
import com.onezol.platform.model.entity.BaseEntity;
import com.onezol.platform.model.param.BaseParam;
import com.onezol.platform.model.param.GenericParam;
import com.onezol.platform.model.pojo.ListResultWrapper;
import com.onezol.platform.service.GenericService;
import com.onezol.platform.util.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Arrays;

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
     * 查询列表： /${controllerName}/list
     *
     * @param param 通用参数
     * @return 结果列表
     */
    @PostMapping("/list")
    public ListResultWrapper<? extends BaseDTO> list(@RequestBody @Validated GenericParam param) {
        ListResultWrapper<T> resultWrapper = service.queryList(param);
        return ConvertUtils.convertTo(resultWrapper, clazz);
    }

    /**
     * 保存/更新： /${controllerName}/save
     *
     * @param param 通用参数
     * @return 保存/更新后的实体
     */
    @PostMapping("/save")
    public BaseDTO save(@RequestBody P param) {
        T entity = service.save(param);
        return ConvertUtils.convertTo(entity, clazz);
    }

    /**
     * 删除： /${controllerName}/delete/${ids}/${physical}
     *
     * @param physicalDelete 是否物理删除
     * @param ids            逗号分隔的id列表
     */
    @DeleteMapping("/{physical}/{ids}")
    public void delete(@PathVariable("physical") boolean physicalDelete, @PathVariable String ids) {
        Long[] idList = Arrays.stream(ids.split(",")).map(Long::parseLong).toArray(Long[]::new);
        service.delete(idList, physicalDelete);
    }
}