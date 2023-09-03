package com.onezol.vertex.security.biz.handler;

import com.onezol.vertex.common.model.record.GenericResponse;
import com.onezol.vertex.common.util.JsonUtils;
import com.onezol.vertex.common.util.JwtUtils;
import com.onezol.vertex.common.util.ResponseUtils;
import com.onezol.vertex.common.util.StringUtils;
import com.onezol.vertex.security.api.service.OnlineUserService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.onezol.vertex.common.constant.Constants.AUTHORIZATION_HEADER;


/**
 * 注销成功处理类
 */
@Component
public class UserLogoutSuccessHandler implements LogoutSuccessHandler {
    private final OnlineUserService onlineUserService;

    public UserLogoutSuccessHandler(OnlineUserService onlineUserService) {
        this.onlineUserService = onlineUserService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 从请求头中获取token
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(authorizationHeader) && authorizationHeader.startsWith(AUTHORIZATION_HEADER)) {
            String token = authorizationHeader.substring(AUTHORIZATION_HEADER.length());
            String subject = JwtUtils.getSubjectFromToken(token);

            // 清除Redis缓存
            onlineUserService.removeOnlineUser(subject);
        }
        // 清除上下文
        SecurityContextHolder.clearContext();

        // 注销成功, 返回成功信息
        String json = JsonUtils.toJson(GenericResponse.success("注销成功"));
        ResponseUtils.write(response, json);
    }
}