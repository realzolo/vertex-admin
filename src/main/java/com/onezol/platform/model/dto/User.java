package com.onezol.platform.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.onezol.platform.model.pojo.DictType;

import java.time.LocalDateTime;
import java.util.Date;

public class User extends BaseDTO {
    private Long id;
    private String username;
    private String nickname;
    private String name;
    private String introduction;
    private String avatar;
    private DictType gender;
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date birthday;
    private String phone;
    private String email;
    private DictType role;
    private DictType status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime createdAt;

    public User() {
    }

    public User(Long id, Long id1, String username, String nickname, String name, String introduction, String avatar, DictType gender, Date birthday, String phone, String email, DictType role, DictType status, LocalDateTime createdAt) {
        super(id);
        this.id = id1;
        this.username = username;
        this.nickname = nickname;
        this.name = name;
        this.introduction = introduction;
        this.avatar = avatar;
        this.gender = gender;
        this.birthday = birthday;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public DictType getGender() {
        return gender;
    }

    public void setGender(DictType gender) {
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

    public DictType getRole() {
        return role;
    }

    public void setRole(DictType role) {
        this.role = role;
    }

    public DictType getStatus() {
        return status;
    }

    public void setStatus(DictType status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
