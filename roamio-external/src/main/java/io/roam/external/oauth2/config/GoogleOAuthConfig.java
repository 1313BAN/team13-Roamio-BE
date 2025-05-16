package io.roam.external.oauth2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Getter
@Configuration
public class GoogleOAuthConfig {
    public static final String AUTHORIZATION = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String GRANT_TYPE = "authorization_code";
    public static final String CONTENT_TYPE = "Content-type";
    public static final String CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded;charset=utf-8";


    public static final String GOOGLE_CODE_URI = "https://accounts.google.com/o/oauth2/v2/auth";
    public static final String GOOGLE_TOKEN_URI = "https://oauth2.googleapis.com/token";
    public static final String GOOGLE_USER_INFO_URI = "https://www.googleapis.com/userinfo/v2/me";


    @Value("${oauth2.google.client-id}")
    private String clientId;

    @Value("${oauth2.google.client-secret}")
    private String clientSecret;

    @Value("${oauth2.google.redirect-uri}")
    private String redirectUri;
}