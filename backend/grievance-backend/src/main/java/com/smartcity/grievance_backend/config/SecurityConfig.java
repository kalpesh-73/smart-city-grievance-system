package com.smartcity.grievance_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF so H2 Console and APIs work
                .csrf(csrf -> csrf.disable())

                // Allow frames so H2 Console can render
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))

                // Allow all requests for now (we'll add JWT later)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}