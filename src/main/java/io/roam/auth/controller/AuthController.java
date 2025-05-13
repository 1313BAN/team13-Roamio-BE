package io.roam.auth.controller;

import io.roam.auth.dto.request.SignInRequest;
import io.roam.auth.dto.request.SignUpRequest;
import io.roam.auth.dto.response.SignUpResponse;
import io.roam.auth.service.AuthService;
import io.roam.common.response.ApiResponse;
import io.roam.jwt.entity.JwtToken;
import io.roam.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final AuthService authService;
    private final UserService userService;

    @Override
    @PostMapping("/signin")
    public ApiResponse<JwtToken> signIn(@Valid SignInRequest request) {
        return ApiResponse.of(authService.signIn(request));
    }

    @Override
    @PostMapping("/signup")
    public ApiResponse<SignUpResponse> signUp(@Valid SignUpRequest request) {
        return ApiResponse.of(authService.signUp(request));
    }
}
