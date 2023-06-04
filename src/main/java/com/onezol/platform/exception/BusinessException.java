package com.onezol.platform.exception;


import com.onezol.platform.constant.enums.HttpStatus;

/**
 * 业务异常，用于抛出业务相关的异常。此异常信息需要反馈给前端展示
 */
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(org.springframework.http.HttpStatus httpStatus) {
        super(httpStatus.getReasonPhrase());
        this.code = httpStatus.value();
    }

    public BusinessException(HttpStatus httpStatus) {
        super(httpStatus.getValue());
        this.code = httpStatus.getCode();
    }

    public BusinessException(org.springframework.http.HttpStatus httpStatus, String message) {
        super(message);
        this.code = httpStatus.value();
    }

    public BusinessException(HttpStatus httpStatus, String message) {
        super(message);
        this.code = httpStatus.getCode();
    }

    public BusinessException(String message) {
        super(message);
        this.code = HttpStatus.FAILURE.getCode();
    }

    public int getCode() {
        return code;
    }
}
