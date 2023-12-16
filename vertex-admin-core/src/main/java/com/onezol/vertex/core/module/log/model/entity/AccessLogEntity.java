package com.onezol.vertex.core.module.log.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.common.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_access_log")
public class AccessLogEntity extends BaseEntity {
    private Long userId;
    private String userType;
    private String userName;
    private String module;
    private String action;
    private String description;
    private String path;
    private String method;
    private String params;
    private Long time;
    private String ip;
    private String location;
    private String browser;
    private String os;
    private Boolean success;
    private String failureReason;
}
