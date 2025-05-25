package io.roam.plan.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import io.roam.plan.entity.PlanCollaboratorInfo;
import lombok.Builder;

@Builder
public record PlanResponse(
    Long id,
    String ownerEmail,
    String ownerName,
    String title,
    String description,
    LocalDateTime startDate,
    LocalDateTime endDate,
    List<PlanCollaboratorInfo> collaborators
) {
}