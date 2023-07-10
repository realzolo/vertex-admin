package com.onezol.vertex.security.fillter;

import com.onezol.vertex.common.util.StringUtils;
import com.onezol.vertex.core.cache.RedisCache;
import com.onezol.vertex.security.model.dto.UserIdentity;
import com.onezol.vertex.security.util.JwtUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static com.onezol.vertex.common.constant.CommonConstant.P_AUTHORIZATION_HEADER;
import static com.onezol.vertex.security.constant.RedisConstant.USER_PREFIX;

/**
 * JWT token过滤器 验证token有效性
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private final RedisCache redisCache;

    public JwtAuthenticationTokenFilter(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从请求头中获取token
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(authorizationHeader) && authorizationHeader.startsWith(P_AUTHORIZATION_HEADER)) {
            String token = authorizationHeader.substring(P_AUTHORIZATION_HEADER.length());

            // 验证token有效性
            boolean ok = JwtUtils.validateToken(token);
            if (!ok) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            // 从Redis中获取用户信息
            UserIdentity user = redisCache.getCacheObject(USER_PREFIX + token);

            if (Objects.isNull(user)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            // 将用户信息存入SecurityContext中, 以便后续使用
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // 放行
        filterChain.doFilter(request, response);
    }
}
