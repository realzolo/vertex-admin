package com.onezol.vertex.app.handler;

import com.onezol.vertex.common.model.record.AjaxResult;
import com.onezol.vertex.common.util.JsonUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.*;
import java.util.stream.Collectors;

import static com.onezol.vertex.common.constant.CommonConstant.S_CONTROLLER_NULL_RESP;
@ControllerAdvice
public class ResponseBodyHandler implements ResponseBodyAdvice<Object> {

    /**
     * 判断是否需要对响应进行处理
     *
     * @param returnType    返回类型
     * @param converterType 转换类型
     * @return 是否需要处理
     */
    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
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
        // 如果mediaType不是json，则不进行处理
        if (!Objects.equals(mediaType.getSubtype(), "json")) {
            return body;
        }

        // controller 返回 void
        if (Objects.isNull(body)) {
            return AjaxResult.success(null);
        }
        // controller 返回 AjaxResult
        if (body instanceof AjaxResult || body instanceof ResponseEntity) {
            return body;
        }
        // controller 返回 String
        if (body instanceof String) {
            // 返回 null
            if (Objects.equals(body, S_CONTROLLER_NULL_RESP)) {
                return JsonUtils.toJson(AjaxResult.success(null));
            }
            return JsonUtils.toJson(AjaxResult.success(body));
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
        return AjaxResult.success(body);
    }
}