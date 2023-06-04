package com.onezol.platform.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.platform.constant.enums.AccountStatus;
import com.onezol.platform.constant.enums.Gender;

import java.time.LocalDateTime;
import java.util.Date;


@TableName(value = "USER")
public class UserEntity extends BaseEntity {
    @TableField("USERNAME")
    private String username;

    @TableField("PASSWORD")
    private String password;

    @TableField("NICKNAME")
    private String nickname;

    @TableField("NAME")
    private String name;

    @TableField("INTRODUCTION")
    private String introduction;

    @TableField("AVATAR")
    private String avatar;

    @TableField("GENDER")
    private Gender gender;

    @TableField("BIRTHDAY")
    private Date birthday;

    @TableField("PHONE")
    private String phone;

    @TableField("EMAIL")
    private String email;

    @TableField("STATUS")
    private AccountStatus status;

    public UserEntity() {
    }

    public UserEntity(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, boolean deleted, String username, String password, String nickname, String name, String introduction, String avatar, Gender gender, Date birthday, String phone, String email, AccountStatus status) {
        super(id, createdAt, updatedAt, deleted);
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.introduction = introduction;
        this.avatar = avatar;
        this.gender = gender;
        this.birthday = birthday;
        this.phone = phone;
        this.email = email;
        this.status = status;
    }

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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
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
