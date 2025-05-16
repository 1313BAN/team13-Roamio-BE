package io.roam.auth.dto.response;

import io.roam.jwt.entity.JwtToken;
import lombok.Builder;

@Builder
public record SignInResponse(
    String name,
    String email,
    JwtToken token
) {
}
