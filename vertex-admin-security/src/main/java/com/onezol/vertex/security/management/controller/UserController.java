package com.onezol.vertex.security.management.controller;

import com.onezol.vertex.common.model.dto.DTO;
import com.onezol.vertex.common.model.payload.GenericPayload;
import com.onezol.vertex.common.model.record.ListResultWrapper;
import com.onezol.vertex.core.common.controller.GenericController;
import com.onezol.vertex.core.util.ModelUtils;
import com.onezol.vertex.security.management.model.dto.User;
import com.onezol.vertex.security.management.model.entity.UserEntity;
import com.onezol.vertex.security.management.model.payload.UserUpdatePayload;
import com.onezol.vertex.security.management.service.UserService;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/list")
    protected ListResultWrapper<? extends DTO> queryList(@RequestBody GenericPayload payload) {
        return super.queryList(payload);
    }

    /**
     * 更新： /xxx/save
     */
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
