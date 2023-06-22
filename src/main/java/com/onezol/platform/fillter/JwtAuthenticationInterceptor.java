package com.onezol.platform.fillter;

import com.nimbusds.jose.JOSEException;
import com.onezol.platform.common.UserContextHolder;
import com.onezol.platform.model.dto.User;
import com.onezol.platform.service.UserService;
import com.onezol.platform.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.onezol.platform.constant.Constant.P_AUTHORIZATION_HEADER;
import static com.onezol.platform.constant.Constant.P_RK_USER;

/**
 * JWT 认证拦截器
 * <br/>
 * HandlerInterceptor执行顺序：preHandle -> Controller -> postHandle -> afterCompletion
 */
@Component
@SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection"})
public class JwtAuthenticationInterceptor implements HandlerInterceptor, Ordered {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 拦截器执行顺序, 值越小越先执行
     *
     * @return 顺序
     */
    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(P_AUTHORIZATION_HEADER)) {
            String token = authorizationHeader.substring(P_AUTHORIZATION_HEADER.length());
            try {
                if (JwtUtils.validateToken(token)) {
                    String username = JwtUtils.getUsernameFromToken(token);
                    // 从Redis中获取用户信息
                    User user = (User) redisTemplate.opsForValue().get(P_RK_USER + username);
                    if (user != null) {
                        UserContextHolder.setUser(user);
                        return true;
                    }
                    // 从数据库中获取用户信息
                    user = userService.getUserByUsername(username);
                    if (user != null) {
                        UserContextHolder.setUser(user);
                        return true;
                    }
                }
            } catch (JOSEException e) {
                // Token 无效，拒绝访问
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return false;
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) throws Exception {
        UserContextHolder.clear();
    }
}
