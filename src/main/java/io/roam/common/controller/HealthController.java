package io.roam.common.controller;

import io.roam.common.response.BaseResponse;
import io.roam.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping("/ok")
    public ApiResponse<String> ok() {
        return ApiResponse.of(HttpStatus.OK, "ok!!");
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
