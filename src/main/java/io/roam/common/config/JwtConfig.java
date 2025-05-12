package io.roam.common.config;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Configuration
public class JwtConfig {
    @Value("${app.jwt.secret}")
    public String SECRET;

    @Value("${app.jwt.access-token-expiration}")
    public long ACCESS_TOKEN_EXPIRATION;

    @Value("${app.jwt.refresh-token-expiration}")
    public long REFRESH_TOKEN_EXPIRATION;

    public SecretKey createSecretKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }
}