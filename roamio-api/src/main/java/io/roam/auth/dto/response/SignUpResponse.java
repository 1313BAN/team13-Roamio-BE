package io.roam.auth.dto.response;

import lombok.Builder;

@Builder
public record SignUpResponse(
    String userId,
    String email,
    String name,
    String userRole
) {
}
