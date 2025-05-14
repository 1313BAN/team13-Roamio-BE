package io.roam.auth.dto.request;

import io.roam.user.entity.SocialType;

public record SocialSignUpRequest(
    String token,
    SocialType socialType,
    String name
) {
}
