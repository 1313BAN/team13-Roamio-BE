package io.roam.user.dto.request;

public record SignInRequest(
    String email,
    String password
) {
}
