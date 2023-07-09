package com.onezol.vertex.api.controller;

import com.onezol.vertex.common.model.BaseDTO;
import com.onezol.vertex.core.annotation.ControllerService;
import com.onezol.vertex.core.model.dto.Permission;
import com.onezol.vertex.core.model.entity.PermissionEntity;
import com.onezol.vertex.core.model.param.DeleteParam;
import com.onezol.vertex.core.model.param.PermissionParam;
import com.onezol.vertex.core.service.PermissionService;
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
//    @PreAuthorize("admin:permission:save")
    public BaseDTO save(@RequestBody PermissionParam param) {
        return super.save(param);
    }

    /**
     * 删除： /{controllerName}/delete
     *
     * @param param 删除参数
     */
    @Override
//    @PreAuthorize("admin:permission:delete")
    public void delete(@RequestBody DeleteParam param) {
        super.delete(param);
    }
}
