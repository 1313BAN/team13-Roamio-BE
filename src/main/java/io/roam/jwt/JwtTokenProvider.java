package io.roam.jwt;

import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.roam.common.config.JwtConfig;
import io.roam.jwt.entity.JwtPayload;
import io.roam.jwt.exception.ExpiredTokenException;
import io.roam.jwt.exception.InvalidTokenException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtConfig jwtConfig;

    // 헤더 관련 상수
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    // 클레임 관련 상수
    private static final String CLIENT_ID_CLAIM = "clientId";
    private static final String SOCIAL_TYPE_CLAIM = "socialType";
    private static final String ROLE_CLAIM = "role";


    /**
     * 인증 정보를 기반으로 JWT 토큰을 생성합니다.
     */
    public String generateToken(JwtPayload payload, Long expirationTime) {
        // 사용자 정보 클레임 생성
        Map<String, Object> claims = Map.of(
            CLIENT_ID_CLAIM, payload.clientId(),
            SOCIAL_TYPE_CLAIM, payload.socialType(),
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
     * HTTP 요청에서 토큰을 추출합니다.
     * Bearer 접두사가 있으면 제거하고, 없으면 토큰 그대로 반환합니다.
     */
    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        if (token == null) {
            return null;
        }
        return token.startsWith(BEARER_PREFIX) ? token.substring(BEARER_PREFIX.length()) : token;
    }

    /**
     * 토큰이 유효한지 검증합니다.
     */
    public boolean validateToken(String token) {
        return token != null && getClaims(token) != null;
    }

    /**
     * 토큰에서 클라이언트 아이디 정보를 추출합니다.
     */
    public String getClientId(String token) {
        Claims claims = getClaims(token);
        return claims != null ? getClientId(claims) : null;
    }

    /**
     * Claims에서 클라이언트 아이디 정보를 추출합니다.
     */
    public String getClientId(Claims claims) {
        return claims.get(CLIENT_ID_CLAIM, String.class);
    }
    
    /**
     * 토큰에서 소셜 타입 정보를 추출합니다.
     */
    public String getSocialType(String token) {
        Claims claims = getClaims(token);
        return claims != null ? getSocialType(claims) : null;
    }

    /**
     * Claims에서 소셜 타입 정보를 추출합니다.
     */
    public String getSocialType(Claims claims) {
        return claims.get(SOCIAL_TYPE_CLAIM, String.class);
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
        try {
            return Jwts.parser()
                .verifyWith(jwtConfig.createSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException();
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }
}