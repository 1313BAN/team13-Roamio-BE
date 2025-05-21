package io.roam.external.oauth2.client.google;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import io.roam.external.oauth2.config.OAuth2FeignConfig;
import io.roam.external.oauth2.dto.response.GoogleUserInfoResponse;

@Component
@FeignClient(name = "googleUserInfoClient", url = "https://www.googleapis.com", configuration = OAuth2FeignConfig.class)
public interface GoogleUserInfoClient {

    @GetMapping(value = "/oauth2/v2/userinfo")
    GoogleUserInfoResponse getUserInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization);
} 