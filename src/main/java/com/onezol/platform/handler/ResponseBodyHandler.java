package com.onezol.platform.handler;

import com.onezol.platform.model.pojo.AjaxResult;
import com.onezol.platform.util.JsonUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

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
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
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
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> converterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {
        if (body instanceof AjaxResult || body instanceof ResponseEntity) {
            return body;
        }
        if (body instanceof String) {
            return JsonUtils.toJson(AjaxResult.success(body));
        }
        String bodyString = body.toString();
        if (bodyString.startsWith("{") && bodyString.endsWith("}")) {
            String[] vars = bodyString.split(",");
            for (String var : vars) {
                String[] kv = var.split("=");
                String k = (kv[0].startsWith("{") ? kv[0].substring(1).trim() : kv[0]).trim();
                String v = (kv[1].endsWith("}") ? kv[1].substring(0, kv[1].length() - 1) : kv[1]).trim();
                if (Objects.equals(k, "status") && !Objects.equals(v, "200")) {
                    return body;
                }
            }
        }
        return AjaxResult.success(body);
    }
}
