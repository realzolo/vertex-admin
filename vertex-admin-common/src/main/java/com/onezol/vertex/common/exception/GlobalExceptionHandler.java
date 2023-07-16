package com.onezol.vertex.common.exception;

import com.onezol.vertex.common.constant.enums.HttpStatus;
import com.onezol.vertex.common.model.record.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * BusinessException 用于处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public AjaxResult<?> businessExceptionHandler(BusinessException e) {
        return AjaxResult.failure(e.getCode(), e.getMessage());
    }

    /**
     * SQLException 用于处理数据库异常
     */
    @ExceptionHandler(SQLException.class)
    public AjaxResult<?> sqlExceptionHandler(SQLException e) {
        logger.error("数据库异常：{}", e.getLocalizedMessage());
        return AjaxResult.failure(HttpStatus.DATABASE_ERROR, e.getMessage());
    }

    /**
     * HttpMessageNotReadableException 用于处理请求参数格式错误
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public AjaxResult<?> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        logger.error("参数解析失败：{}", e.getMessage());
        return AjaxResult.failure(HttpStatus.PARAM_ERROR);
    }
}
