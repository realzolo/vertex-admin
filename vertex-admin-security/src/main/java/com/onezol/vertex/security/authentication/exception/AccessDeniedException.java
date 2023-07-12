package com.onezol.vertex.security.authentication.exception;

import com.onezol.vertex.common.constant.enums.HttpStatus;
import com.onezol.vertex.common.exception.BusinessException;

/**
 * 无权限异常
 */
public class AccessDeniedException extends BusinessException {
    public AccessDeniedException() {
        super(HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.getValue());
    }

    public AccessDeniedException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
