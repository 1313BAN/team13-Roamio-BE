package io.roam.external.oauth2.service;

import io.roam.external.oauth2.dto.response.SocialAuthResponse;

public interface OAuthService {
    SocialAuthResponse authorize(String token);
}
