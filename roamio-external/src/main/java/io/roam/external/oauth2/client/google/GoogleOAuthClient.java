package io.roam.external.oauth2.client.google;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.roam.external.oauth2.config.OAuth2FeignConfig;
import io.roam.external.oauth2.dto.response.GoogleOAuthResponse;

import java.util.Map;

@FeignClient(name = "googleAuthClient", url = "https://oauth2.googleapis.com", configuration = OAuth2FeignConfig.class)
public interface GoogleOAuthClient {

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    GoogleOAuthResponse getToken(@RequestBody Map<String, ?> request);
} 