package com.onezol.vertex.security.biz.exception;

import com.onezol.vertex.common.constant.enums.HttpStatus;
import com.onezol.vertex.common.exception.BusinessException;

/**
 * 登录异常
 */
public class LoginException extends BusinessException {
    public LoginException(String message) {
        super(HttpStatus.LOGIN_FAILURE, message);
    }
}