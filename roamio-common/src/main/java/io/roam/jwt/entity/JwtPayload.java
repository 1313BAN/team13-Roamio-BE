package io.roam.jwt.entity;

import io.roam.user.type.SocialType;
import io.roam.user.type.UserRole;
import lombok.Builder;

@Builder
public record JwtPayload(
    String clientId,
    SocialType socialType,
    UserRole role
) {
}
