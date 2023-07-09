package com.onezol.vertex.api.controller;

import com.onezol.vertex.common.model.BaseDTO;
import com.onezol.vertex.common.pojo.ListResultWrapper;
import com.onezol.vertex.core.annotation.ControllerService;
import com.onezol.vertex.core.model.dto.PermissionGroup;
import com.onezol.vertex.core.model.entity.PermissionGroupEntity;
import com.onezol.vertex.core.model.param.DeleteParam;
import com.onezol.vertex.core.model.param.GenericParam;
import com.onezol.vertex.core.model.param.PermissionGroupParam;
import com.onezol.vertex.core.service.PermissionGroupService;
import com.onezol.vertex.core.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/permission-group")
@ControllerService(service = PermissionGroupService.class, retClass = PermissionGroup.class)
public class PermissionGroupController extends GenericController<PermissionGroupEntity, PermissionGroupParam> {

    @Autowired
    private PermissionGroupService permissionGroupService;

    /**
     * 查询列表： /{controllerName}/list
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
     * 保存/更新： /{controllerName}/save
     *
     * @param param 通用参数
     * @return 保存/更新后的实体
     */
    @Override
//    @PreAuthorize("admin:permission:save")
    public BaseDTO save(@RequestBody PermissionGroupParam param) {
        boolean autoGeneratePermission = !Objects.isNull(param.getAutoGeneratePermission()) && param.getAutoGeneratePermission();
        PermissionGroup permissionGroup = ModelUtils.convert(param, PermissionGroup.class);
        return permissionGroupService.createPermissionGroup(permissionGroup, autoGeneratePermission);
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
