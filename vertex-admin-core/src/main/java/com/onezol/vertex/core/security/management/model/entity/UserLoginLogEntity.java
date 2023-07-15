package com.onezol.vertex.core.security.management.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.common.model.entity.BaseEntity;

import java.time.LocalDateTime;

@TableName("sys_user_login_log")
public class UserLoginLogEntity extends BaseEntity {
    /**
     * 用户账号
     */
    private String username;

    /**
     * 登录状态
     */
    private String status;

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
     * 提示消息
     */
    private String message;

    /**
     * 访问时间
     */
    private LocalDateTime time;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
