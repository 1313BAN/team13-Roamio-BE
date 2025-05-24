package io.roam.websocket.plan.dto;

import lombok.Builder;

@Builder
public record PlanLeaveResponse(
    String userId
) {
}
