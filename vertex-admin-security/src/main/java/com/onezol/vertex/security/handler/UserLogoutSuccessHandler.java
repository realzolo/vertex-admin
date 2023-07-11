package com.onezol.vertex.security.handler;

import com.onezol.vertex.common.pojo.AjaxResult;
import com.onezol.vertex.common.util.JsonUtils;
import com.onezol.vertex.common.util.ResponseUtils;
import com.onezol.vertex.common.util.StringUtils;
import com.onezol.vertex.core.cache.RedisCache;
import com.onezol.vertex.security.util.JwtUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.onezol.vertex.common.constant.CommonConstant.P_AUTHORIZATION_HEADER;
import static com.onezol.vertex.security.constant.RedisConstant.USER_PREFIX;

/**
 * 注销成功处理类
 */
@Component
public class UserLogoutSuccessHandler implements LogoutSuccessHandler {
    private final RedisCache redisCache;

    public UserLogoutSuccessHandler(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 从请求头中获取token
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(authorizationHeader) && authorizationHeader.startsWith(P_AUTHORIZATION_HEADER)) {
            String token = authorizationHeader.substring(P_AUTHORIZATION_HEADER.length());
            String subject = JwtUtils.getSubjectFromToken(token);

            // 清除Redis缓存
            redisCache.deleteObject(USER_PREFIX + subject);
        }
        // 清除上下文
        SecurityContextHolder.clearContext();

        // 注销成功, 返回成功信息
        String json = JsonUtils.toJson(AjaxResult.success("注销成功"));
        ResponseUtils.write(response, json);
    }
}
