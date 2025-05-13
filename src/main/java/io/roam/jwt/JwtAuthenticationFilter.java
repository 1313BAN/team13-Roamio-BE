package io.roam.jwt;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.roam.user.entity.UserRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
                                    throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);
        authenticateUserIfValidToken(token);
        filterChain.doFilter(request, response);
    }
    
    /**
     * 유효한 토큰인 경우 사용자를 인증합니다.
     */
    private void authenticateUserIfValidToken(String token) {
        if (token == null) {
            return;
        }
        
        Claims claims = jwtTokenProvider.getClaims(token);
        if (claims != null) {
            String email = jwtTokenProvider.getEmail(claims);
            String role = jwtTokenProvider.getRole(claims);
            
            if (email != null && role != null) {
                Authentication authentication = UserAuthentication.of(email, UserRole.valueOf(role));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
    }
}
