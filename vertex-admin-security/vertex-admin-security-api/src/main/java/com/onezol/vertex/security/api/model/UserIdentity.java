package com.onezol.vertex.security.api.model;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.onezol.vertex.common.constant.enums.AccountStatus;
import com.onezol.vertex.common.util.CodecUtils;
import com.onezol.vertex.common.util.ObjectConverter;
import com.onezol.vertex.security.api.model.dto.User;
import com.onezol.vertex.security.api.model.entity.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class UserIdentity implements UserDetails {
    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime loginTime;

    /**
     * 登录IP地址
     */
    private String ip;

    /**
     * 登录地点
     */
    private String location;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 角色列表
     */
    private Set<String> roles;

    /**
     * 权限列表
     */
    private Set<String> permissions;

    /**
     * 用户信息
     */
    private UserEntity user;

    /**
     * 权限列表
     */
    private Set<GrantedAuthority> authorities;

    public UserIdentity(UserEntity user) {
        this.user = user;
    }

    @Override
    @JSONField(serialize = false)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities == null) {
            authorities = permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
        }
        return authorities;
    }

    @Override
    @JSONField(serialize = false)
    public String getPassword() {
        String password = user.getPassword();
        user.setPassword(null);
        return password;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonExpired() {
        return user.getStatus() != AccountStatus.EXPIRED;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonLocked() {
        return user.getStatus() != AccountStatus.LOCKED;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isEnabled() {
        return user.getStatus() != AccountStatus.DISABLED;
    }

    public String getKey() {
        return CodecUtils.encodeBase64(user.getId().toString() + "@" + user.getUsername());
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public void setRoles(Set<String> roles) {
        this.roles = Objects.isNull(roles) ? Collections.emptySet() : roles;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = Objects.isNull(permissions) ? Collections.emptySet() : permissions;
    }

    public User getUser() {
        return ObjectConverter.convert(user, User.class);
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
