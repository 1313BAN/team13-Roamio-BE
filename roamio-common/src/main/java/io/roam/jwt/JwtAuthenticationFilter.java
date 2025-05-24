package io.roam.jwt;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.roam.common.exception.DomainException;
import io.roam.jwt.exception.JwtAuthenticationException;
import io.roam.user.type.UserRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
                                    throws ServletException, IOException {
        try {
            String token = jwtTokenProvider.resolveToken(request);
            authenticateUserIfValidToken(token);
            filterChain.doFilter(request, response);
        } catch (DomainException e) {
            log.error("e.getMessage(): {}", e);
            authenticationEntryPoint.commence(request, response, new JwtAuthenticationException(e));
            return;
        }
    }
    
    /**
     * 유효한 토큰인 경우 사용자를 인증합니다.
     */
    private void authenticateUserIfValidToken(String token) {
        if (token == null) {
            return;
        }

        Claims claims = jwtTokenProvider.getClaims(token);
        String userId = jwtTokenProvider.getUserId(claims);
        String socialType = jwtTokenProvider.getSocialType(claims);
        String role = jwtTokenProvider.getRole(claims);

        log.info("userId: {}", userId);
        log.info("socialType: {}", socialType);
        log.info("role: {}", role);
        
        Authentication authentication = UserAuthentication.of(socialType, userId, UserRole.valueOf(role));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
