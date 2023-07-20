package com.onezol.vertex.core.security.management.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.onezol.vertex.common.model.dto.BaseDTO;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class OnlineUser extends BaseDTO {
    private Long uid;

    private String username;

    private String nickname;

    private String avatar;

    private String ip;

    private String browser;

    private String os;

    private String location;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime loginTime;

    private String onlineTime;

    private Boolean isAdministrator;
}
