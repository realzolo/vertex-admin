package com.onezol.vertex.security.controller;

import com.onezol.vertex.common.exception.BusinessException;
import com.onezol.vertex.common.pojo.ListResultWrapper;
import com.onezol.vertex.core.common.controller.GenericController;
import com.onezol.vertex.core.common.model.dto.DTO;
import com.onezol.vertex.core.common.model.param.GenericParam;
import com.onezol.vertex.core.util.ModelUtils;
import com.onezol.vertex.security.model.dto.User;
import com.onezol.vertex.security.model.entity.UserEntity;
import com.onezol.vertex.security.model.param.UserUpdateParam;
import com.onezol.vertex.security.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController extends GenericController<UserService> {
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
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     * 查询列表： /xxx/list
     */
    @Override
    protected ListResultWrapper<? extends DTO> queryList(GenericParam param) {
        return super.queryList(param);
    }

    @PutMapping
    public boolean update(@RequestBody UserUpdateParam param) {
        if (param.getId() == null) {
            throw new BusinessException("用户ID不能为空");
        }
        UserEntity entity = ModelUtils.convert(param, UserEntity.class);
        return userService.updateById(entity);
    }

    /**
     * 删除： /{controllerName}/delete
     */
    @Override
    public void delete(@RequestBody GenericParam param) {
        param.setPhysicalDelete(false);
        super.delete(param);
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
