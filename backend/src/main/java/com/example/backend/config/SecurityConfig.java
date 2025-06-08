package com.example.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())          // 关闭 CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()          // 所有请求都放行
                )
                .formLogin(form -> form.disable())     // 禁用表单登录
                .httpBasic(httpBasic -> httpBasic.disable()); // 禁用 basic 登录

        return http.build();
    }
}
