package io.roam.external.oauth2.dto.request;

import feign.form.FormProperty;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class GoogleOAuthRequest {
    private String code;
    @FormProperty("client_id")
    private String clientId;
    @FormProperty("client_secret")
    private String clientSecret;
    @FormProperty("redirect_uri")
    private String redirectUri;
    @FormProperty("grant_type")
    private String grantType;
}