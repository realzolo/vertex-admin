package com.onezol.vertex.core.common.handler;

import com.onezol.vertex.common.exception.BusinessException;
import com.onezol.vertex.common.model.record.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * BusinessException 用于处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public GenericResponse<?> businessExceptionHandler(BusinessException e) {
        return GenericResponse.failure(e.getCode(), e.getLocalizedMessage());
    }
}
