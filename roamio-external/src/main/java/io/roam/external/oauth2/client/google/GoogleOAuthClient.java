package io.roam.external.oauth2.client.google;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.roam.external.oauth2.dto.request.GoogleOAuthRequest;
import io.roam.external.oauth2.dto.response.GoogleOAuthResponse;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Component
@FeignClient(name = "googleAuthClient", url = "https://oauth2.googleapis.com")
public interface GoogleOAuthClient {

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    GoogleOAuthResponse getToken(@RequestBody Map<String, ?> request);
} 