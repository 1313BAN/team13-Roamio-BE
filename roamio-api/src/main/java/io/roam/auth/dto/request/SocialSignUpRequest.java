package io.roam.auth.dto.request;

import io.roam.user.type.SocialType;

public record SocialSignUpRequest(
    String token,
    SocialType socialType,
    String name
) {
}
