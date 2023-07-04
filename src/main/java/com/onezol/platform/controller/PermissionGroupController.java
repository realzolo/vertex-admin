package com.onezol.platform.controller;

import com.onezol.platform.annotation.ControllerService;
import com.onezol.platform.model.dto.BaseDTO;
import com.onezol.platform.model.dto.PermissionGroup;
import com.onezol.platform.model.entity.PermissionGroupEntity;
import com.onezol.platform.model.param.GenericParam;
import com.onezol.platform.model.param.PermissionGroupParam;
import com.onezol.platform.model.pojo.ListResultWrapper;
import com.onezol.platform.service.PermissionGroupService;
import com.onezol.platform.util.ConvertUtils;
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
     * 查询列表： /${controllerName}/list
     *
     * @param param 通用参数
     * @return 结果列表
     */
    @Override
//    @PreAuthorize("admin:permission:list")
    public ListResultWrapper<? extends BaseDTO> list(GenericParam param) {
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
    public BaseDTO save(@RequestBody PermissionGroupParam param) {
        boolean autoGeneratePermission = !Objects.isNull(param.getAutoGeneratePermission()) && param.getAutoGeneratePermission();
        PermissionGroup permissionGroup = ConvertUtils.convertTo(param, PermissionGroup.class);
        return permissionGroupService.createPermissionGroup(permissionGroup, autoGeneratePermission);
    }

    /**
     * 删除： /${controllerName}/delete/${ids}/${physical}
     *
     * @param physicalDelete 是否物理删除
     * @param ids            逗号分隔的id列表
     */
    @Override
//    @PreAuthorize("admin:permission:delete")
    public void delete(boolean physicalDelete, String ids) {
        super.delete(physicalDelete, ids);
    }
}
