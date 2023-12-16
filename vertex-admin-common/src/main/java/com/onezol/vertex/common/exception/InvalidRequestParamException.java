package com.onezol.vertex.common.exception;

import com.onezol.vertex.common.constant.enums.HttpStatus;

/**
 * 无效请求参数异常
 */
public class InvalidRequestParamException extends BusinessException {
    public InvalidRequestParamException() {
        super(HttpStatus.PARAM_ERROR);
    }

    public InvalidRequestParamException(String message) {
        super(HttpStatus.PARAM_ERROR, message);
    }
}
