package io.roam.plan.dto.response;

import java.util.List;

import lombok.Builder;


@Builder
public record PlanListResponse(
    List<PlanResponse> planList
) {
}
