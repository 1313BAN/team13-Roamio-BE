package io.roam.websocket.plan.dto;

import lombok.Builder;

@Builder
public record PlanEnterResponse(
    String userId,
    String name
) {    
}
