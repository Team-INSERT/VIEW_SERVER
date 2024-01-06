package com.insert.view.infra.config.security;

import com.insert.view.infra.error.ExceptionFilter;
import com.insert.view.infra.jwt.auth.JwtAuth;
import com.insert.view.infra.jwt.auth.JwtFilter;
import com.insert.view.infra.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class FilterConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtUtil jwtUtil;
    private final JwtAuth jwtAuth;

    @Override
    public void configure(HttpSecurity builder) {
        JwtFilter jwtFilter = new JwtFilter(jwtAuth, jwtUtil);
        ExceptionFilter globalExceptionFilter = new ExceptionFilter();
        builder.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        builder.addFilterBefore(globalExceptionFilter, JwtFilter.class);
    }
}
