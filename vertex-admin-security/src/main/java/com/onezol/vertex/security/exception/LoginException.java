package com.onezol.vertex.security.exception;

import com.onezol.vertex.common.constant.enums.HttpStatus;
import com.onezol.vertex.common.exception.BusinessException;

/**
 * 登录异常
 */
public class LoginException extends BusinessException {
    public LoginException() {
        super(HttpStatus.LOGIN_FAILURE, HttpStatus.LOGIN_FAILURE.getValue());
    }

    public LoginException(String message) {
        super(HttpStatus.LOGIN_FAILURE, message);
    }
}
