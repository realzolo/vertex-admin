package com.onezol.vertex.app.controller;

import com.onezol.vertex.common.annotation.PreAuthorize;
import com.onezol.vertex.common.exception.BusinessException;
import com.onezol.vertex.common.model.BaseDTO;
import com.onezol.vertex.common.model.BaseParam;
import com.onezol.vertex.common.pojo.ListResultWrapper;
import com.onezol.vertex.common.util.RegexUtils;
import com.onezol.vertex.core.annotation.ControllerService;
import com.onezol.vertex.core.model.param.DeleteParam;
import com.onezol.vertex.core.model.param.GenericParam;
import com.onezol.vertex.core.util.ModelUtils;
import com.onezol.vertex.security.model.dto.User;
import com.onezol.vertex.security.model.entity.UserEntity;
import com.onezol.vertex.security.model.param.UserSigninParam;
import com.onezol.vertex.security.model.param.UserSignupParam;
import com.onezol.vertex.security.model.param.UserUpdateParam;
import com.onezol.vertex.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@ControllerService(service = UserService.class, retClass = User.class)
public class UserController extends GenericController<UserEntity, BaseParam> {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public User signup(@RequestBody @Validated UserSignupParam param) {
        return userService.signup(param);
    }

    @PostMapping("/signin")
    public Map<String, Object> signin(@RequestBody @Validated UserSigninParam param) {
        String type = param.getType();
        switch (type) {
            case "account":
                if (param.getUsername() == null || param.getPassword() == null) {
                    throw new BusinessException("用户名或密码不能为空");
                }
                return userService.signinByAccount(param.getUsername(), param.getPassword());
            case "email":
                if (param.getEmail() == null || param.getCaptcha() == null) {
                    throw new BusinessException("邮箱或验证码不能为空");
                }
                return userService.signinByEmail(param.getEmail(), param.getCaptcha());
            default:
                throw new BusinessException("不支持的登录类型");
        }
    }

    @PostMapping("/send-email-code/{email}")
    public void sendEmailCode(@PathVariable String email) {
        boolean ok = RegexUtils.isEmail(email);
        if (!ok) {
            throw new BusinessException("邮箱格式不正确");
        }
        userService.sendEmailCode(email);
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
