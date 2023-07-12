package com.onezol.vertex.security.authentication.fillter;

import com.onezol.vertex.common.util.JwtUtils;
import com.onezol.vertex.common.util.ResponseUtils;
import com.onezol.vertex.common.util.StringUtils;
import com.onezol.vertex.core.cache.RedisCache;
import com.onezol.vertex.security.authentication.exception.AuthenticationException;
import com.onezol.vertex.security.authentication.model.UserIdentity;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
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
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.onezol.vertex.common.constant.CommonConstant.AUTHORIZATION_HEADER;
import static com.onezol.vertex.common.constant.RedisConstant.USER_PREFIX;


/**
 * JWT token过滤器: 验证token有效性、token续期
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    public static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);
    private final RedisCache redisCache;

    /**
     * token过期时间: 默认1小时 （单位秒）
     */
    @Value("${spring.jwt.expiration-time:3600}")
    private Integer expirationTime;

    /**
     * token续期的阈值: 默认15分钟 （单位秒, 距离过期时间小于该阈值时, 进行续期操作）
     */
    @Value("${spring.jwt.renew-threshold:900}")
    private Integer renewThreshold;


    public JwtAuthenticationTokenFilter(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从请求头中获取token
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(authorizationHeader) && authorizationHeader.startsWith(AUTHORIZATION_HEADER)) {
            String token = authorizationHeader.substring(AUTHORIZATION_HEADER.length());

            // 验证token有效性
            boolean ok = JwtUtils.validateToken(token);
            if (!ok) {
                ResponseUtils.write(response, new AuthenticationException("用户会话已过期, 请重新登录"));
                return;
            }

            // 从Redis中获取用户信息, 并验证用户信息是否存在
            String subject = JwtUtils.getSubjectFromToken(token);
            UserIdentity user = redisCache.getCacheObject(USER_PREFIX + subject);
            if (Objects.isNull(user)) {
                ResponseUtils.write(response, new AuthenticationException("用户会话已过期, 请重新登录"));
                return;
            }

            // 检查token是否快要过期，进行续期操作
            Claims claims = JwtUtils.getClaimsFromToken(token);
            assert claims != null;
            if (isTokenNearExpiration(claims)) {
                // 续期JWT
                String renewedToken = renewToken(claims);
                response.setHeader(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER + renewedToken);
                // 续期Redis中的用户信息
                boolean expire = redisCache.expire(USER_PREFIX + subject, expirationTime, TimeUnit.SECONDS);
                logger.info("用户[{}]的token已续期", subject);
            }

            // 将用户信息存入SecurityContext中, 以便后续使用
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // 放行
        filterChain.doFilter(request, response);
    }

    /**
     * 检查token是否快要过期
     *
     * @param claims JWT的Claims对象
     * @return true: 快要过期, false: 没有快要过期
     */
    private boolean isTokenNearExpiration(Claims claims) {
        // 获取token的过期时间
        Date expirationDate = claims.getExpiration();
        // 计算token剩余有效时间（单位：毫秒）
        long timeToExpiration = expirationDate.getTime() - System.currentTimeMillis();
        // 判断token是否快要过期
        return timeToExpiration < renewThreshold * 1000;
    }

    /**
     * 续期JWT（重新生成一个）
     *
     * @param claims JWT的Claims对象
     * @return 续期后的JWT
     */
    private String renewToken(Claims claims) {
        String subject = claims.getSubject();
        // 进行JWT续期操作(重新生成一个)
        return JwtUtils.generateToken(subject);
    }
}
