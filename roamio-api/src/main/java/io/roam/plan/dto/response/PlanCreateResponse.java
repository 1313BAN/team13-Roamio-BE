package io.roam.plan.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import io.roam.plan.entity.PlanCollaboratorInfo;
import lombok.Builder;

@Builder
public record PlanCreateResponse(
    Long id,
    String title,
    String description,
    LocalDateTime startDate,
    LocalDateTime endDate,
    List<PlanCollaboratorInfo> collaborators
) {
}
