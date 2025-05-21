package io.roam.external.oauth2.config;

import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Response;
import feign.codec.ErrorDecoder;
import io.roam.external.oauth2.exception.AuthenticationFailedException;
import io.roam.external.oauth2.exception.UserInfoApiCallFailedException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class OAuth2FeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new OAuth2ErrorDecoder();
    }

    @Slf4j
    public static class OAuth2ErrorDecoder implements ErrorDecoder {
        @Override
        public Exception decode(String methodKey, Response response) {
            log.error("OAuth2 API 호출 실패: {}, 상태 코드: {}", methodKey, response.status());
            try {
                String bodyContent = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
                log.info("Response Body: {}", bodyContent);
            } catch (Exception e) {
                log.error("Response Body 읽기 실패");
            }

            if (methodKey.contains("#getToken")) {
                return new AuthenticationFailedException();
            } else if (methodKey.contains("#getUserInfo")) {
                return new UserInfoApiCallFailedException();
            }
            
            return new Default().decode(methodKey, response);
        }
    }
}