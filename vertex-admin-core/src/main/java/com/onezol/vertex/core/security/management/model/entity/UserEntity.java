package com.onezol.vertex.core.security.management.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.onezol.vertex.common.constant.enums.AccountStatus;
import com.onezol.vertex.common.constant.enums.Gender;
import com.onezol.vertex.common.model.entity.BaseEntity;

import java.time.LocalDate;

@ApiSupport
@TableName("sys_user")
public class UserEntity extends BaseEntity {

    private String username;

    private String password;

    private String nickname;

    private String name;

    private String introduction;

    private String avatar;

    private Gender gender;

    private LocalDate birthday;

    private String phone;

    private String email;

    private LocalDate pwdExpDate;

    private AccountStatus status;
}
