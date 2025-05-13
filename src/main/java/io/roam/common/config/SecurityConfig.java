package io.roam.common.config;

import lombok.RequiredArgsConstructor;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.roam.common.response.ExceptionResponse;
import io.roam.jwt.JwtAuthenticationFilter;
import io.roam.jwt.exception.JwtErrorCode;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)

                // 세션 사용하지 않음
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // URL 권한 설정
                .authorizeHttpRequests(authorizeRequest -> authorizeRequest
                        // swagger
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/**").permitAll()

                        // health check
                        .requestMatchers("/health/**").permitAll()

                        // 인증
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                    // 인증 실패 (401 Unauthorized)
                    .authenticationEntryPoint((request, response, authException) -> handleException(response, JwtErrorCode.AUTHENTICATION_REQUIRED))
                    // 권한 실패 (403 Forbidden)
                    .accessDeniedHandler((request, response, accessDeniedException) -> handleException(response, JwtErrorCode.ACCESS_DENIED))
                );


        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * 예외 응답을 처리하는 메서드
     */
    private void handleException(HttpServletResponse response, JwtErrorCode errorCode) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorCode.getHttpStatus().value());
        response.setCharacterEncoding("UTF-8");
        
        ExceptionResponse exceptionResponse = ExceptionResponse.of(errorCode);
        response.getWriter().write(objectMapper.writeValueAsString(exceptionResponse));
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
