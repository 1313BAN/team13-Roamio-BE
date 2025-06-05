package io.roam.plan.dto.request;

import java.time.LocalDateTime;

public record PlanCreateRequest(
    String title,
    String description,
    LocalDateTime startDate,
    LocalDateTime endDate
) {
}
