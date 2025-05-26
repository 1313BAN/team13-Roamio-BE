package io.roam.plan.dto.response;

import java.util.List;

import lombok.Builder;

@Builder
public record PlanBlueprintsResponse(
    List<PlanBlueprintResponse> blueprints
) {
}
