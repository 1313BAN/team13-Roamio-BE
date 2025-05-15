package io.roam.jwt.entity;

import io.roam.user.entity.SocialType;
import lombok.Builder;

@Builder
public record AuthUserDetail(
    String clientId,
    SocialType socialType
) {
}
