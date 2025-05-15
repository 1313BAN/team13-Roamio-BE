package io.roam.auth.controller;

import io.roam.auth.dto.request.SignInRequest;
import io.roam.auth.dto.request.SignUpRequest;
import io.roam.auth.dto.request.SocialSignInRequest;
import io.roam.auth.dto.response.SignInResponse;
import io.roam.auth.dto.response.SignUpResponse;
import io.roam.common.response.ApiResponse;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;


public interface AuthApi {

    public ApiResponse<SignInResponse> signIn(@RequestBody SignInRequest request);

    public ApiResponse<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request);

    public ApiResponse<SignInResponse> socialSignIn(@RequestBody SocialSignInRequest request);

    public ApiResponse<String> callback();
}
