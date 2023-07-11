package com.onezol.vertex.app.controller;

import com.onezol.vertex.common.annotation.PreAuthorize;
import com.onezol.vertex.common.exception.BusinessException;
import com.onezol.vertex.common.model.BaseDTO;
import com.onezol.vertex.common.model.BaseParam;
import com.onezol.vertex.common.pojo.ListResultWrapper;
import com.onezol.vertex.core.annotation.ControllerService;
import com.onezol.vertex.core.model.param.DeleteParam;
import com.onezol.vertex.core.model.param.GenericParam;
import com.onezol.vertex.core.util.ModelUtils;
import com.onezol.vertex.security.model.dto.User;
import com.onezol.vertex.security.model.entity.UserEntity;
import com.onezol.vertex.security.model.param.UserUpdateParam;
import com.onezol.vertex.security.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@ControllerService(service = UserService.class, retClass = User.class)
public class UserController extends GenericController<UserEntity, BaseParam> {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 查询: /{controllerName}/{id}
     *
     * @param id 主键
     * @return 结果
     */
    @Override
    @GetMapping("/{id}")
    public BaseDTO getById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     * 查询列表： /{controllerName}/list
     *
     * @param param 通用参数
     * @return 结果列表
     */
    @Override
    @PreAuthorize("admin:user:list")
    public ListResultWrapper<? extends BaseDTO> list(@RequestBody GenericParam param) {
        return super.list(param);
    }

    @PutMapping
    @PreAuthorize("admin:user:update")
    public boolean update(@RequestBody UserUpdateParam param) {
        if (param.getId() == null) {
            throw new BusinessException("用户ID不能为空");
        }
        UserEntity entity = ModelUtils.convert(param, UserEntity.class);
        return userService.updateById(entity);
    }

    /**
     * 删除： /{controllerName}/delete
     *
     * @param param 删除参数
     */
    @Override
    @PreAuthorize("admin:user:delete")
    public void delete(@RequestBody DeleteParam param) {
        param.setPhysicalDelete(false);
        super.delete(param);
    }
}
