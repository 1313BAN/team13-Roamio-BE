package io.roam.websocket.plan.dto;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record PlanDateUpdateResponse(
    Long planId,
    LocalDateTime startDate,
    LocalDateTime endDate,
    String action
) {
} 