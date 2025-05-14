package io.roam.auth.dto.request;

import io.roam.user.entity.SocialType;

public record SocialSignInRequest(
    SocialType socialType,
    String token
) {
}
