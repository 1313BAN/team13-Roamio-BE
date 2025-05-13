package io.roam.auth.controller;

import io.roam.auth.dto.request.SignInRequest;
import io.roam.auth.dto.request.SignUpRequest;
import io.roam.auth.dto.response.SignUpResponse;
import io.roam.common.response.ApiResponse;
import io.roam.common.response.BaseResponse;
import io.roam.jwt.entity.JwtToken;

public interface AuthApi {

    public ApiResponse<JwtToken> signIn(SignInRequest request);

    public ApiResponse<SignUpResponse> signUp(SignUpRequest request);
}
