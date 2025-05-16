package io.roam.external.oauth2.service;

import org.springframework.stereotype.Service;

import io.roam.external.oauth2.client.google.GoogleOAuthClient;
import io.roam.external.oauth2.client.google.GoogleUserInfoClient;
import io.roam.external.oauth2.config.GoogleOAuthConfig;
import io.roam.external.oauth2.dto.request.GoogleOAuthRequest;
import io.roam.external.oauth2.dto.response.GoogleOAuthResponse;
import io.roam.external.oauth2.dto.response.GoogleUserInfoResponse;
import io.roam.external.oauth2.dto.response.SocialAuthResponse;
import io.roam.user.type.SocialType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthService implements OAuthService {

    private final GoogleOAuthConfig googleOAuthConfig;
    private final GoogleOAuthClient googleOAuthClient;
    private final GoogleUserInfoClient googleUserInfoClient;

    @Override
    public SocialAuthResponse authorize(String token) {
        log.info("Google OAuth authorize called with token: {}", token);
        
        GoogleOAuthRequest googleOAuthRequest = GoogleOAuthRequest.builder()
            .code(token)
            .clientId(googleOAuthConfig.getClientId())
            .clientSecret(googleOAuthConfig.getClientSecret())
            .redirectUri(googleOAuthConfig.getRedirectUri())
            .grantType(GoogleOAuthConfig.GRANT_TYPE)
            .build();

        log.info("Google OAuth Request: {}", googleOAuthRequest);

        // Feign Client를 사용하여 액세스 토큰 발급
        GoogleOAuthResponse tokenResponse;
        try {
            tokenResponse = googleOAuthClient.getToken(googleOAuthRequest.toMap());
        } catch (Exception e) {
            log.error("Google OAuth Authentication Failed: {}", e.getMessage());
            throw new RuntimeException("Google OAuth Authentication Failed", e);
        }

        if (tokenResponse == null) {
            throw new RuntimeException("tokenResponse is null");
        }
        
        log.info("Google OAuth Token Response: {}", tokenResponse);
            
        // Feign Client를 사용하여 사용자 정보 API 호출
        GoogleUserInfoResponse userInfoResponse;
        try {
            userInfoResponse = googleUserInfoClient.getUserInfo("Bearer " + tokenResponse.accessToken());
        } catch (Exception e) {
            log.error("Google User Info API Call Failed: {}", e.getMessage());
            throw new RuntimeException("Google User Info API Call Failed", e);
        }
        
        if (userInfoResponse == null) {
            throw new RuntimeException("userInfoResponse is null");
        }
        
        log.info("Google User Info Response: {}", userInfoResponse);
        
        return SocialAuthResponse.builder()
            .socialType(SocialType.GOOGLE)
            .socialId(userInfoResponse.id())
            .email(userInfoResponse.email())
            .name(userInfoResponse.name())
            .profileImageUrl(userInfoResponse.picture())
            .build();
    }
}
