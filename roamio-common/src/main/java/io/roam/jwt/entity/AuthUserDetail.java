package io.roam.jwt.entity;

import io.roam.user.type.SocialType;
import lombok.Builder;

@Builder
public record AuthUserDetail(
    String clientId,
    SocialType socialType
) {
}
