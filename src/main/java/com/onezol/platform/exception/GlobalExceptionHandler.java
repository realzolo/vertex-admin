package com.onezol.platform.exception;

import com.onezol.platform.model.pojo.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public AjaxResult<?> businessExceptionHandler(BusinessException e) {
        logger.error("业务异常：{}", e.getMessage());
        return AjaxResult.failure(10500, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public AjaxResult<?> runtimeExceptionHandler(RuntimeException e) {
        e.printStackTrace();
        return AjaxResult.failure(500, e.getMessage());
    }
}
