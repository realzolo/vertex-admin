package com.onezol.vertex.security.config;

import com.onezol.vertex.security.fillter.JwtAuthenticationTokenFilter;
import com.onezol.vertex.security.handler.AuthenticationEntryPointIHandler;
import com.onezol.vertex.security.handler.UserLogoutSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class WebSecurityConfig {

    /**
     * JWT 认证过滤器
     */
    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    /**
     * 认证入口点处理器(认证失败处理器)
     */
    private final AuthenticationEntryPointIHandler authenticationEntryPointIHandler;

    /**
     * 用户注销处理器
     */
    private final UserLogoutSuccessHandler userLogoutSuccessHandler;


    public WebSecurityConfig(JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter, AuthenticationEntryPointIHandler authenticationEntryPointIHandler, UserLogoutSuccessHandler userLogoutSuccessHandler) {
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
        this.authenticationEntryPointIHandler = authenticationEntryPointIHandler;
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
        return httpSecurity
                // 授权请求
                .authorizeRequests()
                // 放行请求
                .antMatchers("/auth/*").permitAll()
                // 其他请求需要认证
                .anyRequest().authenticated().and()
                // JWT 认证过滤器
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // 认证入口点处理器
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPointIHandler).and()
                // Logout 处理器
                .logout().logoutUrl("/logout").logoutSuccessHandler(userLogoutSuccessHandler).and()
                // 禁用 csrf
                .csrf().disable()
                // 禁用 session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 禁用 frameOptions
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
