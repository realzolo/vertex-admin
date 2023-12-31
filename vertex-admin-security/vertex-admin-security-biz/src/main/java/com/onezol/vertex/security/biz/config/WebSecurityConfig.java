package com.onezol.vertex.security.biz.config;

import com.onezol.vertex.common.util.ControllerPathUtils;
import com.onezol.vertex.security.api.annotation.RestrictAccess;
import com.onezol.vertex.security.biz.fillter.JwtAuthenticationTokenFilter;
import com.onezol.vertex.security.biz.handler.AuthenticationEntryPointIHandler;
import com.onezol.vertex.security.biz.handler.UserAccessDeniedHandler;
import com.onezol.vertex.security.biz.handler.UserLogoutSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
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

/**
 * Spring Security 配置类
 */
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
        // 获取限制访问路径并转换为 MvcMatcher 请求认证
        Set<String> restrictPathSet = ControllerPathUtils.getControllerPaths(RestrictAccess.class);
        Set<String> mvcMatchers = ControllerPathUtils.convertPathToMvcMatcher(restrictPathSet);
        String[] restrictPaths = mvcMatchers.toArray(new String[0]);

        return httpSecurity
                // 禁用 csrf
                .csrf().disable()
                // 禁用 session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 配置请求
                .authorizeRequests()
                // 放行WebSocket请求
                .antMatchers("/ws/**").permitAll()
                // 静态资源，可匿名访问
                .antMatchers(HttpMethod.GET, "/", "/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/profile/**").permitAll()
                .antMatchers("/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/*/api-docs/**", "/druid/**").permitAll()
                // 需要认证的接口
                .mvcMatchers(restrictPaths).authenticated().and()
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