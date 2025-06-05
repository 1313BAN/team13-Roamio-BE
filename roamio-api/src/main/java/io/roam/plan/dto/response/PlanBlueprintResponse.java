package io.roam.plan.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record PlanBlueprintResponse(
    Long id,
    Long planId,
    Integer day,
    BigDecimal position,
    String placeId,
    Double latitude,
    Double longitude,
    LocalDateTime startTime,
    LocalDateTime endTime,
    String memo
) {
}
