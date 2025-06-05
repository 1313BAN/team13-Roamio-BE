package io.roam.websocket.plan.dto.request;

import java.time.LocalDateTime;

public record PlanDateUpdateRequest(
    LocalDateTime startDate,
    LocalDateTime endDate
) {
} 