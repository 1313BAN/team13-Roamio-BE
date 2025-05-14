package io.roam.external.oauth2.dto.response;

import io.roam.user.entity.SocialType;
import lombok.Builder;

@Builder
public record SocialAuthResponse(
    SocialType socialType,
    String socialId,
    String email,
    String name,
    String profileImageUrl
) {
}
