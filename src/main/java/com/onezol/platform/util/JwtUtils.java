package com.onezol.platform.util;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtils {
    public static long EXPIRE = 2 * 60 * 60 * 1000; // 过期时间, 单位: 毫秒, 默认: 2小时
    private static String SECRET = "E08B28641AFE4425937E7B3315FE673F"; // 密钥

    /**
     * 生成 Token
     *
     * @param username 用户名
     * @return Token
     * @throws JOSEException JOSE 异常
     */
    public static String generateToken(String username) throws JOSEException {
        JWSSigner signer = new MACSigner(SECRET);

        Instant now = Instant.now();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .expirationTime(Date.from(now.plusMillis(EXPIRE)))
                .issueTime(Date.from(now))
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

    /**
     * 刷新 Token
     *
     * @param token 原始 Token
     * @return 新的 Token
     * @throws JOSEException  JOSE 异常
     * @throws ParseException 解析异常
     */
    public static String refreshToken(String token) throws JOSEException, ParseException {
        if (StringUtils.hasText(token)) {
            throw new JOSEException("Token 不可为空");
        }

        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(SECRET);

        if (!signedJWT.verify(verifier)) {
            throw new JOSEException("Token 验证失败");
        }

        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
        Instant now = Instant.now();

        if (claimsSet.getExpirationTime().before(Date.from(now.plusMillis(EXPIRE / 2)))) {
            // 如果 JWT 即将过期，则重新生成一个新的 JWT
            return generateToken(claimsSet.getSubject());
        } else {
            // 否则，返回原始 JWT
            return token;
        }
    }

    /**
     * 从 Token 中获取用户名
     *
     * @param token Token
     * @return 用户名
     * @throws JOSEException  JOSE 异常
     * @throws ParseException 解析异常
     */
    public static String getUsernameFromToken(String token) throws JOSEException, ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(SECRET);
        if (signedJWT.verify(verifier)) {
            return signedJWT.getJWTClaimsSet().getSubject();
        } else {
            throw new JOSEException("Token 验证失败");
        }
    }

    /**
     * 验证 Token 是否有效
     *
     * @param token Token
     * @return 是否有效
     * @throws JOSEException  JOSE 异常
     * @throws ParseException 解析异常
     */
    public static boolean validateToken(String token) throws JOSEException, ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(SECRET);
        return signedJWT.verify(verifier);
    }

    @Value("${encryption.jwt.secret}")
    public void setSecret(String secret) {
        // 判断 secret 是否符合要求
        if (secret.length() < 32) {
            throw new IllegalArgumentException("The secret length must be at least 256 bits");
        }
        JwtUtils.SECRET = secret;
    }

    @Value("${encryption.jwt.expire}")
    public void setExpire(Long expire) {
        JwtUtils.EXPIRE = expire;
    }
}
