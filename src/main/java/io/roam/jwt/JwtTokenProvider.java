package io.roam.jwt;

import java.util.Date;
import java.util.Map;

import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.roam.common.config.JwtConfig;
import io.roam.jwt.entity.JwtPayload;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtConfig jwtConfig;

    // 헤더 관련 상수
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    // 클레임 관련 상수
    private static final String EMAIL_CLAIM = "email";
    private static final String ROLE_CLAIM = "role";


    /**
     * 인증 정보를 기반으로 JWT 토큰을 생성합니다.
     */
    public String generateToken(JwtPayload payload, Long expirationTime) {
        // 사용자 정보 클레임 생성
        Map<String, Object> claims = Map.of(
            EMAIL_CLAIM, payload.email(),
            ROLE_CLAIM, payload.role()
        );

        Date now = new Date();

        return Jwts.builder()
            .issuedAt(now)
            .expiration(new Date(now.getTime() + expirationTime))
            .claims(claims)
            .signWith(jwtConfig.createSecretKey())
            .compact();
    }

    /**
     * HTTP 요청에서 Bearer 토큰을 추출합니다.
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        return (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) ? bearerToken.substring(BEARER_PREFIX.length()) : null;
    }

    /**
     * 토큰이 유효한지 검증합니다.
     */
    public boolean validateToken(String token) {
        return token != null && getClaims(token) != null;
    }

    /**
     * 토큰에서 이메일 정보를 추출합니다.
     */
    public String getEmail(String token) {
        Claims claims = getClaims(token);
        return claims != null ? getEmail(claims) : null;
    }

    /**
     * Claims에서 이메일 정보를 추출합니다.
     */
    public String getEmail(Claims claims) {
        return claims.get(EMAIL_CLAIM, String.class);
    }

    /**
     * 토큰에서 역할 정보를 추출합니다.
     */
    public String getRole(String token) {
        Claims claims = getClaims(token);
        return claims != null ? getRole(claims) : null;
    }

    /**
     * Claims에서 역할 정보를 추출합니다.
     */
    public String getRole(Claims claims) {
        return claims.get(ROLE_CLAIM, String.class);
    }

    /**
     * 토큰에서 Claims 정보를 추출합니다.
     * 토큰이 유효하지 않은 경우 null을 반환합니다.
     */
    public Claims getClaims(String token) {
        if (token == null) {
            return null;
        }
        
        try {
            return Jwts.parser()
                .verifyWith(jwtConfig.createSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (Exception e) {
            return null;
        }
    }
}