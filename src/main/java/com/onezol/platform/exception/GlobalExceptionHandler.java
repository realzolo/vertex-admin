package com.onezol.platform.exception;

import com.onezol.platform.model.pojo.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * BusinessException 用于处理业务异常
     *
     * @param e 异常
     * @return AjaxResult
     */
    @ExceptionHandler(BusinessException.class)
    public AjaxResult<?> businessExceptionHandler(BusinessException e) {
        return AjaxResult.failure(10500, e.getMessage());
    }

    /**
     * HttpMessageNotReadableException 用于处理请求参数格式错误
     *
     * @param e 异常
     * @return AjaxResult
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public AjaxResult<?> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        logger.error("参数解析失败：{}", e.getMessage());
        return AjaxResult.failure(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    /**
     * RuntimeException 用于处理运行时异常
     *
     * @param e 异常
     * @return AjaxResult
     */
    @ExceptionHandler(Exception.class)
    public AjaxResult<?> runtimeExceptionHandler(RuntimeException e) {
        e.printStackTrace();
        return AjaxResult.failure(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }
}
