package io.roam.common.controller;

import io.roam.common.exception.TestException;
import io.roam.common.response.ApiResponse;
import io.roam.common.response.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping("/ok")
    public ApiResponse ok() {
        return SuccessResponse.of(HttpStatus.OK, "ok!!");
    }
}
