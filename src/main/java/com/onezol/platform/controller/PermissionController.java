package com.onezol.platform.controller;

import com.onezol.platform.annotation.ControllerService;
import com.onezol.platform.annotation.PreAuthorize;
import com.onezol.platform.model.dto.BaseDTO;
import com.onezol.platform.model.dto.Permission;
import com.onezol.platform.model.entity.PermissionEntity;
import com.onezol.platform.model.param.DeleteParam;
import com.onezol.platform.model.param.PermissionParam;
import com.onezol.platform.service.PermissionService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/permission")
@ControllerService(service = PermissionService.class, retClass = Permission.class)
public class PermissionController extends GenericController<PermissionEntity, PermissionParam> {

    /**
     * 保存/更新： /${controllerName}/save
     *
     * @param param 通用参数
     * @return 保存/更新后的实体
     */
    @Override
    @PreAuthorize("admin:permission:save")
    public BaseDTO save(@RequestBody PermissionParam param) {
        return super.save(param);
    }

    /**
     * 删除： /{controllerName}/delete
     *
     * @param param 删除参数
     */
    @Override
    @PreAuthorize("admin:permission:delete")
    public void delete(@RequestBody DeleteParam param) {
        super.delete(param);
    }
}
