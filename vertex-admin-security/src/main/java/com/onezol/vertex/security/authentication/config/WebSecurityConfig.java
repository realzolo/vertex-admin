package com.onezol.vertex.security.authentication.config;

import com.onezol.vertex.common.annotation.Anonymous;
import com.onezol.vertex.common.util.ControllerPathUtils;
import com.onezol.vertex.security.authentication.fillter.JwtAuthenticationTokenFilter;
import com.onezol.vertex.security.authentication.handler.AuthenticationEntryPointIHandler;
import com.onezol.vertex.security.authentication.handler.UserAccessDeniedHandler;
import com.onezol.vertex.security.authentication.handler.UserLogoutSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Set;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    /**
     * JWT认证过滤器
     */
    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    /**
     * 认证入口点处理器(认证失败处理器)
     */
    private final AuthenticationEntryPointIHandler authenticationEntryPointIHandler;
    /**
     * 拒绝访问处理类(权限不足)
     */
    private final UserAccessDeniedHandler userAccessDeniedHandler;

    /**
     * 用户注销处理器
     */
    private final UserLogoutSuccessHandler userLogoutSuccessHandler;


    public WebSecurityConfig(JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter, AuthenticationEntryPointIHandler authenticationEntryPointIHandler, UserAccessDeniedHandler userAccessDeniedHandler, UserLogoutSuccessHandler userLogoutSuccessHandler) {
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
        this.authenticationEntryPointIHandler = authenticationEntryPointIHandler;
        this.userAccessDeniedHandler = userAccessDeniedHandler;
        this.userLogoutSuccessHandler = userLogoutSuccessHandler;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 安全过滤器链
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // 获取匿名访问路径并转换为 MvcMatcher 放行
        Set<String> anonymousPaths = ControllerPathUtils.getControllerPaths(Anonymous.class);
        Set<String> mvcMatchers = ControllerPathUtils.convertPathToMvcMatcher(anonymousPaths);
        httpSecurity.authorizeRequests().mvcMatchers(mvcMatchers.toArray(new String[0])).permitAll();

        return httpSecurity
                // 禁用 csrf
                .csrf().disable()
                // 禁用 session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 所有请求需要认证
                .authorizeRequests().anyRequest().authenticated().and()
                // Logout 处理器
                .logout().logoutUrl("/logout").logoutSuccessHandler(userLogoutSuccessHandler).and()
                // JWT 认证过滤器
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // 认证失败处理器
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPointIHandler)
                // 权限不足处理器
                .accessDeniedHandler(userAccessDeniedHandler).and()
                .build();
    }

    /**
     * 密码编码器
     *
     * @return 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
