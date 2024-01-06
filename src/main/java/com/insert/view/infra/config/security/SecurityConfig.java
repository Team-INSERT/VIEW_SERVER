package com.insert.view.infra.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insert.view.infra.error.CustomAuthenticationEntryPoint;
import com.insert.view.infra.jwt.auth.JwtAuth;
import com.insert.view.infra.jwt.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private final JwtAuth jwtAuth;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()
                )
                .exceptionHandling(exceptionHandler -> exceptionHandler.authenticationEntryPoint(new CustomAuthenticationEntryPoint(objectMapper)))
                .apply(new FilterConfig(jwtUtil,jwtAuth));

        return http.build();
    }
}
