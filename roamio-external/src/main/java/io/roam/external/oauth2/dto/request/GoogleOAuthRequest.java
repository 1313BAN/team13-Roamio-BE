package io.roam.external.oauth2.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.Map;

@Builder
public record GoogleOAuthRequest(
    String code,
    String clientId,
    String clientSecret,
    String redirectUri,
    String grantType
) {
    public Map<String, String> toMap() {
        return Map.of(
            "code", code,
            "client_id", clientId,
            "client_secret", clientSecret,
            "redirect_uri", redirectUri,
            "grant_type", grantType
        );
    }
}