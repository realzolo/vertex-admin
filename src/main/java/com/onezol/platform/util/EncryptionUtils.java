package com.onezol.platform.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.salt.RandomSaltGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class EncryptionUtils {

    private static int ENCRYPTION_ITERATIONS = 1000;
    private static String ENCRYPTION_PASSWORD = "FA1F437D388544B8B848F1AAD8BF52F0";

    /**
     * 对字符串进行 PBE 可逆加密
     * 示例：encrypt("123456") = "U2FsdGVkX1+XQ3Z4Qj5Q8Q=="
     *
     * @param str 待加密的字符串
     * @return 加密后的字符串
     */
    public static String encrypt(String str) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(ENCRYPTION_PASSWORD);
        encryptor.setAlgorithm("PBEWithHmacSHA512AndAES_256"); // 加密算法
        encryptor.setKeyObtentionIterations(ENCRYPTION_ITERATIONS); // 迭代次数
        encryptor.setSaltGenerator(new RandomSaltGenerator()); // 随机盐生成器
        return encryptor.encrypt(str);
    }

    /**
     * 对字符串进行解密
     * 示例：decrypt("U2FsdGVkX1+XQ3Z4Qj5Q8Q==") = "123456"
     */
    public static String decrypt(String str) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(ENCRYPTION_PASSWORD);
        encryptor.setAlgorithm("PBEWithHmacSHA512AndAES_256"); // 加密算法
        encryptor.setKeyObtentionIterations(ENCRYPTION_ITERATIONS); // 迭代次数
        encryptor.setSaltGenerator(new RandomSaltGenerator()); // 随机盐生成器
        return encryptor.decrypt(str);
    }

    /**
     * 对字符串进行 SHA-512 不可逆加密
     * 示例：encryptSha512("123456") = "7c4a8d09ca3762af61e59520943dc26494f8941b"
     *
     * @param str 待加密的字符串
     * @return 加密后的字符串
     */
    public static String encryptSha512(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(str.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(Integer.toHexString((b & 0xFF) + 0x100).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Encryption error", e);
        }
    }

    @Value("${encryption.password}")
    public void setEncryptionPassword(String password) {
        EncryptionUtils.ENCRYPTION_PASSWORD = password;
    }

    @Value("${encryption.iterations}")
    public void setEncryptionIterations(int iterations) {
        if (iterations < 0 || iterations > 10000) {
            throw new IllegalArgumentException("Encryption iterations must be between 0 and 10000");
        }
        EncryptionUtils.ENCRYPTION_ITERATIONS = iterations;
    }
}
