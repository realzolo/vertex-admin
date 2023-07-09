package com.onezol.vertex.security.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.common.annotation.DictDefinition;
import com.onezol.vertex.common.constant.enums.AccountStatus;
import com.onezol.vertex.common.model.BaseEntity;

import java.time.LocalDate;

@TableName(value = "pf_user")
public class UserEntity extends BaseEntity {
    private String username;

    private String password;

    private String nickname;

    private String name;

    private String introduction;

    private String avatar;

    @DictDefinition("gender")
    private Integer gender;

    private LocalDate birthday;

    private String phone;

    private String email;

    private AccountStatus status;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}
