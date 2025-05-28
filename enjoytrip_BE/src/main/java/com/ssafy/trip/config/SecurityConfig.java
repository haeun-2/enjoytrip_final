package com.ssafy.trip.config;

import com.ssafy.trip.security.auth.EmailPasswordAuthenticationProvider;
import com.ssafy.trip.security.auth.OAuthAuthenticationProvider;
import com.ssafy.trip.security.errorhandler.CustomAccessDeniedHandler;
import com.ssafy.trip.security.errorhandler.CustomAuthenticationEntryPoint;
import com.ssafy.trip.security.filter.CustomSessionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomSessionFilter sessionFilter;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final OAuthAuthenticationProvider oAuthAuthenticationProvider;
    private final EmailPasswordAuthenticationProvider emailPasswordAuthenticationProvider;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .anyRequest().permitAll()
                );
        http.formLogin(form -> form.disable());
        http.httpBasic(httpBasic -> httpBasic.disable());
        http.addFilterBefore(sessionFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling(e -> e
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
        );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(emailPasswordAuthenticationProvider)
                .authenticationProvider(oAuthAuthenticationProvider)
                .build();
    }
}
