package com.onezol.vertex.app.config;

import com.onezol.vertex.app.interceptor.JwtAuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MVC配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private static final String[] EXCLUDE_PATHS = {
            "/user/sign*",
            "/user/send-email-code/*",
            "/dict-value",
            "/dictionary",
            "/role/select-options",
            "/enum/select-options",
            "/file/temp/*",
    };
    @Autowired
    private JwtAuthenticationInterceptor jwtAuthenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(jwtAuthenticationInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_PATHS)
        ;
    }

}