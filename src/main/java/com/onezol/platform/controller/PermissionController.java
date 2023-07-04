package com.onezol.platform.controller;

import com.onezol.platform.annotation.ControllerService;
import com.onezol.platform.model.dto.BaseDTO;
import com.onezol.platform.model.dto.Permission;
import com.onezol.platform.model.entity.PermissionEntity;
import com.onezol.platform.model.param.GenericParam;
import com.onezol.platform.model.param.PermissionParam;
import com.onezol.platform.model.pojo.ListResultWrapper;
import com.onezol.platform.service.PermissionService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/permission")
@ControllerService(service = PermissionService.class, retClass = Permission.class)
public class PermissionController extends GenericController<PermissionEntity, PermissionParam> {

    /**
     * 查询列表： /${controllerName}/list
     *
     * @param param 通用参数
     * @return 结果列表
     */
    @Override
//    @PreAuthorize("admin:permission:list")
    public ListResultWrapper<? extends BaseDTO> list(@RequestBody GenericParam param) {
        return super.list(param);
    }

    /**
     * 保存/更新： /${controllerName}/save
     *
     * @param param 通用参数
     * @return 保存/更新后的实体
     */
    @Override
//    @PreAuthorize("admin:permission:save")
    public BaseDTO save(PermissionParam param) {
        return super.save(param);
    }
}
