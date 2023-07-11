package com.onezol.vertex.security.controller;

import com.onezol.vertex.common.pojo.ListResultWrapper;
import com.onezol.vertex.core.common.controller.GenericController;
import com.onezol.vertex.core.common.model.dto.DTO;
import com.onezol.vertex.core.common.model.param.GenericParam;
import com.onezol.vertex.core.util.ModelUtils;
import com.onezol.vertex.security.model.dto.PermissionGroup;
import com.onezol.vertex.security.model.param.PermissionGroupParam;
import com.onezol.vertex.security.service.PermissionGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/permission-group")
public class PermissionGroupController extends GenericController<PermissionGroupService> {

    @Autowired
    private PermissionGroupService permissionGroupService;

    /**
     * 获取DTO的Class(返回给前端的数据类型, 避免Entity直接暴露给前端)
     *
     * @return DTO Class
     */
    @Override
    protected Class<? extends DTO> getDtoClass() {
        return PermissionGroup.class;
    }

    /**
     * 查询列表： /xxx/list
     */
    @Override
    @PostMapping("/list")
    protected ListResultWrapper<? extends DTO> queryList(@RequestBody GenericParam param) {
        return super.queryList(param);
    }

    /**
     * 保存/更新： /xxx/save
     *
     * @param param 通用参数
     * @return 保存/更新后的实体
     */
    public DTO save(@RequestBody PermissionGroupParam param) {
        boolean autoGeneratePermission = !Objects.isNull(param.getAutoGeneratePermission()) && param.getAutoGeneratePermission();
        PermissionGroup permissionGroup = ModelUtils.convert(param, PermissionGroup.class);
        return permissionGroupService.createPermissionGroup(permissionGroup, autoGeneratePermission);
    }

    /**
     * 删除： /xxx/delete
     */
    @Override
    @PostMapping("/delete")
    protected void delete(@RequestBody GenericParam param) {
        super.delete(param);
    }
}
