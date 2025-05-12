package io.roam.user.dto.request;

public record SignUpRequest(
    String email,
    String password,
    String name
) {
}
