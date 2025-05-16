package io.roam.jwt.entity;

import lombok.Builder;

@Builder
public record JwtToken(
    String accessToken,
    String refreshToken
) {
}
