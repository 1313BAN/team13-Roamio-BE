package io.roam.jwt.entity;

import io.roam.user.entity.UserRole;
import lombok.Builder;

@Builder
public record JwtPayload(
    String email,
    UserRole role
) {
}
