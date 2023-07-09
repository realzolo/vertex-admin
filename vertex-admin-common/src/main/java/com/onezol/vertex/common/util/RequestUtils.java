package com.onezol.vertex.common.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * Request工具类
 */
public class RequestUtils {
    /**
     * 获取当前request
     *
     * @return request
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * 获取当前session
     *
     * @return session
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 获取当前request中的属性
     *
     * @param name 属性名
     * @return 属性值
     */
    public static String getHeader(String name) {
        return getRequest().getHeader(name);
    }
}
