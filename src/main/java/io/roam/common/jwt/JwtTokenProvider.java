package io.roam.common.jwt;

import java.util.Date;
import java.util.Map;

import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.roam.common.config.JwtConfig;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtConfig jwtConfig;

    public String generateToken(Authentication authentication) {
        Map<String, Object> claims = Map.of(
            "email", authentication.getPrincipal(),
            "roles", authentication.getAuthorities()
        );
        Date now = new Date();

        return Jwts.builder()
            .issuedAt(now)
            .expiration(new Date(now.getTime() + jwtConfig.ACCESS_TOKEN_EXPIRATION))
            .claims(claims)
            .signWith(jwtConfig.createSecretKey())
            .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return (bearerToken != null && bearerToken.startsWith("Bearer ")) ? bearerToken.substring(7) : null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(jwtConfig.createSecretKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmail(String token) {
        return getClaims(token).get("email", String.class);
    }

    public String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
            .verifyWith(jwtConfig.createSecretKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}