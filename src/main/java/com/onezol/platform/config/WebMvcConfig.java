package com.onezol.platform.config;

import com.onezol.platform.fillter.JwtAuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MVC配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private JwtAuthenticationInterceptor jwtAuthenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(jwtAuthenticationInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/sign*")
                .excludePathPatterns("/user/send-email-code/*")
        ;
    }

}
