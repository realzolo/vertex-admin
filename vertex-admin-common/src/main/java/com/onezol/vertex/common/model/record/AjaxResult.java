package com.onezol.vertex.common.model.record;


import com.onezol.vertex.common.constant.enums.HttpStatus;

import java.io.Serializable;

/**
 * 统一返回结果类
 */
public class AjaxResult<T> implements Serializable {
    private int code;
    private boolean success;
    private String message;
    private T data;

    public AjaxResult(int code, boolean success, String message, T data) {
        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> AjaxResult<T> success() {
        return new AjaxResult<>(HttpStatus.SUCCESS.getCode(), true, HttpStatus.SUCCESS.getValue(), null);
    }

    public static <T> AjaxResult<T> success(T data) {
        return new AjaxResult<>(HttpStatus.SUCCESS.getCode(), true, HttpStatus.SUCCESS.getValue(), data);
    }

    public static <T> AjaxResult<T> success(String message, T data) {
        return new AjaxResult<>(HttpStatus.SUCCESS.getCode(), true, message, data);
    }

    public static <T> AjaxResult<T> failure() {
        return new AjaxResult<>(HttpStatus.FAILURE.getCode(), false, HttpStatus.FAILURE.getValue(), null);
    }

    public static <T> AjaxResult<T> failure(String message) {
        return new AjaxResult<>(HttpStatus.FAILURE.getCode(), false, message, null);
    }

    public static <T> AjaxResult<T> failure(int code, String message) {
        return new AjaxResult<>(code, false, message, null);
    }

    public static <T> AjaxResult<T> failure(HttpStatus httpStatus) {
        return new AjaxResult<>(httpStatus.getCode(), false, httpStatus.getValue(), null);
    }

    public static <T> AjaxResult<T> failure(HttpStatus httpStatus, String message) {
        return new AjaxResult<>(httpStatus.getCode(), false, message, null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
