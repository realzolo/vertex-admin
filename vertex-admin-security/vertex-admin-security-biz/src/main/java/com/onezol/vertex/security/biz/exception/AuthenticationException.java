package com.onezol.vertex.security.biz.exception;

import com.onezol.vertex.common.constant.enums.HttpStatus;
import com.onezol.vertex.common.exception.BusinessException;

/**
 * 认证异常
 */
public class AuthenticationException extends BusinessException {
    public AuthenticationException() {
        super(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getValue());
    }

    public AuthenticationException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}