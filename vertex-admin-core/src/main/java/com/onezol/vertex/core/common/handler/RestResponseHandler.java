package com.onezol.vertex.core.common.handler;

import com.onezol.vertex.common.annotation.RestResponse;
import com.onezol.vertex.common.model.record.GenericResponse;
import com.onezol.vertex.common.util.JsonUtils;
import com.onezol.vertex.core.common.exception.ControllerReturnNullValueException;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestResponseHandler implements ResponseBodyAdvice<Object> {

    /**
     * 判断是否需要对响应进行处理
     *
     * @param returnType    返回类型
     * @param converterType 转换类型
     * @return 是否需要处理
     */
    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        Class<?> controllerClass = returnType.getDeclaringClass();
        return controllerClass.isAnnotationPresent(RestResponse.class) && controllerClass.isAnnotationPresent(RestController.class);
    }

    /**
     * 响应返回之前的处理
     *
     * @param body          返回值
     * @param returnType    返回类型
     * @param mediaType     媒体类型
     * @param converterType 转换类型
     * @param request       请求
     * @param response      响应
     * @return 处理后的返回值
     */
    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType, @NonNull MediaType mediaType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> converterType, @NonNull ServerHttpRequest request,
                                  @NonNull ServerHttpResponse response) {
        // controller 返回 void
        if (Objects.isNull(body)) {
            return GenericResponse.success();
        }
        // controller 返回 GenericResponse
        if (body instanceof GenericResponse || body instanceof ResponseEntity) {
            return body;
        }
        // controller 返回 String
        if (body instanceof String) {
            return JsonUtils.toJson(GenericResponse.success(body));
        }
        // 处理异常
        if (body instanceof LinkedHashMap && ((LinkedHashMap<?, ?>) body).size() == 4) {
            Set<?> keySet = ((LinkedHashMap<?, ?>) body).keySet();
            Set<String> keys = keySet.stream().map(Object::toString).collect(Collectors.toSet());
            List<String> list = Arrays.asList("status", "timestamp", "path", "error");
            if (keys.containsAll(list)) {
                return body;
            }
        }
        return GenericResponse.success(body);
    }

    @ExceptionHandler(ControllerReturnNullValueException.class)
    public GenericResponse<?> handleControllerReturnNullValueException(ControllerReturnNullValueException ignored) {
        return GenericResponse.success();
    }
}