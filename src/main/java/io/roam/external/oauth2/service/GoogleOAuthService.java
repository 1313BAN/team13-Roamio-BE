package io.roam.external.oauth2.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.roam.external.oauth2.config.GoogleOAuthConfig;
import io.roam.external.oauth2.dto.request.GoogleOAuthRequest;
import io.roam.external.oauth2.dto.response.GoogleOAuthResponse;
import io.roam.external.oauth2.dto.response.GoogleUserInfoResponse;
import io.roam.external.oauth2.dto.response.SocialAuthResponse;
import io.roam.user.entity.SocialType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthService implements OAuthService {

    private final GoogleOAuthConfig googleOAuthConfig;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

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

        // form-urlencoded 형식으로 변환
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        Map<String, String> map = objectMapper.convertValue(googleOAuthRequest, new TypeReference<Map<String, String>>() {});
        map.forEach(formData::add);

        // 액세스 토큰 발급
        GoogleOAuthResponse tokenResponse = restClient.post()
            .uri(GoogleOAuthConfig.GOOGLE_TOKEN_URI)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(formData)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                throw new RuntimeException("Google OAuth Authentication Failed");
            })
            .body(GoogleOAuthResponse.class);

        if (tokenResponse == null) {
            throw new RuntimeException("tokenResponse is null");
        }
        
        log.info("Google OAuth Token Response: {}", tokenResponse);
            
        // 액세스 토큰으로 사용자 정보 API 호출
        GoogleUserInfoResponse userInfoResponse = restClient.get()
            .uri(GoogleOAuthConfig.GOOGLE_USER_INFO_URI)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.accessToken())
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                throw new RuntimeException("Google User Info API Call Failed");
            })
            .body(GoogleUserInfoResponse.class);
        
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
