package com.onezol.vertex.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    /**
     * JWT 密钥
     */
    private static String secretKey;
    /**
     * JWT 过期时间, 单位: 秒
     */
    private static Integer expirationTime;

    /**
     * 生成 JWT
     *
     * @param subject JWT 主题
     * @return JWT
     */
    public static String generateToken(String subject) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime * 1000);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * 验证 JWT
     *
     * @param token JWT
     * @return 是否有效
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // JWT 验证失败
            return false;
        }
    }

    /**
     * 从 JWT 中获取 subject
     *
     * @param token JWT
     * @return subject
     */
    public static String getSubjectFromToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            return claims.getSubject();
        } catch (Exception e) {
            // JWT 解析失败或无效
            return null;
        }
    }

    @Value("${spring.jwt.secret-key}")
    public void setSecret(String secretKey) {
        JwtUtils.secretKey = secretKey;
    }

    @Value("${spring.jwt.expiration-time}")
    public void setExpire(Integer expirationTime) {
        JwtUtils.expirationTime = expirationTime;
    }
}
