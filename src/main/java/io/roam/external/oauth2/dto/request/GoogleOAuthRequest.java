package io.roam.external.oauth2.dto.request;

import lombok.Builder;

@Builder
public record GoogleOAuthRequest(
    String code,
    String clientId,
    String clientSecret,
    String redirectUri,
    String grantType
) {
}
