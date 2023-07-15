package com.onezol.vertex.core.security.management.controller;

import com.onezol.vertex.common.annotation.Loggable;
import com.onezol.vertex.common.model.dto.DTO;
import com.onezol.vertex.common.model.payload.GenericPayload;
import com.onezol.vertex.common.model.record.ListResultWrapper;
import com.onezol.vertex.core.base.controller.GenericController;
import com.onezol.vertex.core.security.management.model.dto.User;
import com.onezol.vertex.core.security.management.model.entity.UserEntity;
import com.onezol.vertex.core.security.management.model.payload.UserUpdatePayload;
import com.onezol.vertex.core.security.management.service.UserService;
import com.onezol.vertex.core.util.ModelUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户管理")
@Loggable
@RestController
@RequestMapping("/user")
public class UserController extends GenericController<UserService> {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 查询: /xxx/query
     *
     * @param payload 查询参数
     */
    @Override
    @Operation(summary = "查询用户", description = "查询用户详情")
    @PostMapping("/query")
    protected DTO queryById(@RequestBody GenericPayload payload) {
        return super.queryById(payload);
    }

    /**
     * 查询列表： /xxx/list
     *
     * @param payload 查询参数
     */
    @Override
    @Operation(summary = "查询用户", description = "查询用户列表")
    @PostMapping("/list")
    protected ListResultWrapper<? extends DTO> queryList(@RequestBody GenericPayload payload) {
        return super.queryList(payload);
    }

    /**
     * 更新： /xxx/save
     */
    @Operation(summary = "更新用户", description = "更新用户")
    @PutMapping("/save")
    protected boolean save(@RequestBody UserUpdatePayload payload) {
        UserEntity user = ModelUtils.convert(payload, UserEntity.class);
        return userService.updateById(user);
    }

    /**
     * 删除： /xxx/delete
     *
     * @param payload 删除参数
     */
    @Override
    @Operation(summary = "删除用户", description = "删除用户")
    @PostMapping("/delete")
    protected void delete(@RequestBody GenericPayload payload) {
        super.delete(payload);
    }

    /**
     * 获取DTO的Class(返回给前端的数据类型, 避免Entity直接暴露给前端)
     *
     * @return DTO Class
     */
    @Override
    protected Class<? extends DTO> getDtoClass() {
        return User.class;
    }
}
