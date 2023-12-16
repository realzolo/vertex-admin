package com.onezol.vertex.security.api.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.common.constant.enums.AccountStatus;
import com.onezol.vertex.common.constant.enums.Gender;
import com.onezol.vertex.common.model.entity.BaseEntity;
import lombok.Data;

import java.time.LocalDate;

@Data
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
