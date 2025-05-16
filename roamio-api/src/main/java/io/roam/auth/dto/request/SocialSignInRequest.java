package io.roam.auth.dto.request;

import io.roam.user.type.SocialType;

public record SocialSignInRequest(
    SocialType socialType,
    String token
) {
}
