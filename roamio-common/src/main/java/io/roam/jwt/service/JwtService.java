package io.roam.jwt.service;

import org.springframework.stereotype.Service;

import io.roam.common.config.JwtConfig;
import io.roam.jwt.JwtTokenProvider;
import io.roam.jwt.entity.JwtPayload;
import io.roam.jwt.entity.JwtToken;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtConfig jwtConfig;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtToken issueToken(JwtPayload payload)  {
        return JwtToken.builder()
            .accessToken(jwtTokenProvider.generateToken(payload, jwtConfig.ACCESS_TOKEN_EXPIRATION))
            .refreshToken(jwtTokenProvider.generateToken(payload, jwtConfig.REFRESH_TOKEN_EXPIRATION))
            .build();
    }
    
}
