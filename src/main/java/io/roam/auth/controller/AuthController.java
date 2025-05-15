package io.roam.auth.controller;

import io.roam.auth.dto.request.SignInRequest;
import io.roam.auth.dto.request.SignUpRequest;
import io.roam.auth.dto.request.SocialSignInRequest;
import io.roam.auth.dto.response.SignInResponse;
import io.roam.auth.dto.response.SignUpResponse;
import io.roam.auth.service.AuthService;
import io.roam.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final AuthService authService;

    @Override
    @PostMapping("/signin")
    public ApiResponse<SignInResponse> signIn(@RequestBody SignInRequest request) {
        return ApiResponse.of(authService.signIn(request));
    }

    @Override
    @PostMapping("/signup")
    public ApiResponse<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        return ApiResponse.of(authService.signUp(request));
    }

    @Override
    @PostMapping("/socialSignIn")
    public ApiResponse<SignInResponse> socialSignIn(@RequestBody SocialSignInRequest request) {
        return ApiResponse.of(authService.socialSignIn(request));
    }

    @Override
    @GetMapping("/callback")
    public ApiResponse<String> callback() {
        return ApiResponse.of("callback");
    }
}
