package io.roam.plan.dto.response;

import lombok.Builder;

@Builder
public record PlanInviteResponse(
    String invitedUserEmail,
    String invitedUserName
) {
} 