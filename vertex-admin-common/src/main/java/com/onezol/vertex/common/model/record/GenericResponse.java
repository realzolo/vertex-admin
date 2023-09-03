package com.onezol.vertex.common.model.record;

import com.onezol.vertex.common.constant.enums.HttpStatus;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * 统一返回结果类
 */
@Data
public class GenericResponse<T> implements Serializable {

    private int code;

    private boolean success;

    private String message;

    private T data;

    private String traceId;

    private String timestamp;

    public GenericResponse(int code, boolean success, String message, T data) {
        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
        this.traceId = UUID.randomUUID().toString().replace("-", "");
        this.timestamp = String.valueOf(System.currentTimeMillis());
    }

    public static <T> GenericResponse<T> success() {
        return new GenericResponse<>(HttpStatus.SUCCESS.getCode(), true, HttpStatus.SUCCESS.getValue(), null);
    }

    public static <T> GenericResponse<T> success(T data) {
        return new GenericResponse<>(HttpStatus.SUCCESS.getCode(), true, HttpStatus.SUCCESS.getValue(), data);
    }

    public static <T> GenericResponse<T> success(String message, T data) {
        return new GenericResponse<>(HttpStatus.SUCCESS.getCode(), true, message, data);
    }

    public static <T> GenericResponse<T> failure() {
        return new GenericResponse<>(HttpStatus.FAILURE.getCode(), false, HttpStatus.FAILURE.getValue(), null);
    }

    public static <T> GenericResponse<T> failure(String message) {
        return new GenericResponse<>(HttpStatus.FAILURE.getCode(), false, message, null);
    }

    public static <T> GenericResponse<T> failure(int code, String message) {
        return new GenericResponse<>(code, false, message, null);
    }

    public static <T> GenericResponse<T> failure(HttpStatus httpStatus) {
        return new GenericResponse<>(httpStatus.getCode(), false, httpStatus.getValue(), null);
    }
}
