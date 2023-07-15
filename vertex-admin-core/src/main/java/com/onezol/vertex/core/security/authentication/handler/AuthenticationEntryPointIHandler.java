package com.onezol.vertex.core.security.authentication.handler;

import com.onezol.vertex.common.util.ResponseUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 安全认证入口点处理器: 用于处理认证过程中的异常
 */
@Component
public class AuthenticationEntryPointIHandler implements AuthenticationEntryPoint {

    /**
     * 当用户访问需要进行身份验证的资源但未通过身份验证时，将触发身份验证异常
     *
     * @param request       请求
     * @param response      响应
     * @param authException 认证异常
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        ResponseUtils.write(response, new com.onezol.vertex.core.security.authentication.exception.AuthenticationException());
    }
}