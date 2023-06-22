package com.onezol.platform.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.platform.constant.enums.AccountStatus;
import com.onezol.platform.constant.enums.Gender;

import java.time.LocalDateTime;


@TableName(value = "pf_user")
public class UserEntity extends BaseEntity {
    private String username;

    private String password;

    private String nickname;

    private String name;

    private String introduction;

    private String avatar;

    private Gender gender;

    private LocalDateTime birthday;

    private String phone;

    private String email;

    private AccountStatus status;

    public UserEntity() {
    }

    public UserEntity(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, boolean deleted, String username, String password, String nickname, String name, String introduction, String avatar, Gender gender, LocalDateTime birthday, String phone, String email, AccountStatus status) {
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

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
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
