package com.onezol.vertex.common.exception;


import com.onezol.vertex.common.constant.enums.HttpStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务异常，用于抛出业务相关的异常。此异常信息需要反馈给前端展示
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException {

    private final int code;
    private final String message;

    public BusinessException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BusinessException(org.springframework.http.HttpStatus httpStatus) {
        this.code = httpStatus.value();
        this.message = httpStatus.getReasonPhrase();
    }

    public BusinessException(HttpStatus httpStatus) {
        this.code = httpStatus.getCode();
        this.message = httpStatus.getValue();
    }

    public BusinessException(org.springframework.http.HttpStatus httpStatus, String message) {
        this.code = httpStatus.value();
        this.message = message;
    }

    public BusinessException(HttpStatus httpStatus, String message) {
        this.code = httpStatus.getCode();
        this.message = message;
    }

    public BusinessException(String message) {
        this.code = HttpStatus.FAILURE.getCode();
        this.message = message;
    }
}
