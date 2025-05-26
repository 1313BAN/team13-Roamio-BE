package io.roam.plan.dto.request;

import java.math.BigDecimal;

public record PlanBlueprintRequest(
    Long id,
    int day,
    BigDecimal position,
    String placeId,
    Double latitude,
    Double longitude
) {
}
