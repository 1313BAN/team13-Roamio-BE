package io.roam.plan.dto.response;

import lombok.Builder;

@Builder
public record PlanCollaboratorResponse(
    String userId,
    String email,
    String name,
    String profileImageUrl
) {
} 