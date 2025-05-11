package io.roam.common.response;

import org.springframework.http.HttpStatus;

public interface ApiResponse {
    HttpStatus status();
}
