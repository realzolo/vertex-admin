package com.onezol.vertex.common.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class BaseDTO implements DTO {
    private Long id;
    private LocalDateTime createdAt;
}
