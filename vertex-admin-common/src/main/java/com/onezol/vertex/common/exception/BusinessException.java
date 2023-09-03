package com.onezol.vertex.common.exception;


import com.onezol.vertex.common.constant.enums.HttpStatus;
import lombok.Getter;

/**
 * 业务异常，用于抛出业务相关的异常。此异常信息需要反馈给前端展示
 */
@Getter
public class BusinessException extends RuntimeException {

    private final int code;
    private final String message;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(org.springframework.http.HttpStatus httpStatus) {
        super(httpStatus.getReasonPhrase());
        this.code = httpStatus.value();
        this.message = httpStatus.getReasonPhrase();
    }

    public BusinessException(HttpStatus httpStatus) {
        super(httpStatus.getValue());
        this.code = httpStatus.getCode();
        this.message = httpStatus.getValue();
    }

    public BusinessException(org.springframework.http.HttpStatus httpStatus, String message) {
        super(message);
        this.code = httpStatus.value();
        this.message = message;
    }

    public BusinessException(HttpStatus httpStatus, String message) {
        super(message);
        this.code = httpStatus.getCode();
        this.message = httpStatus.getValue();
    }

    public BusinessException(String message) {
        super(message);
        this.code = HttpStatus.FAILURE.getCode();
        this.message = message;
    }
}
