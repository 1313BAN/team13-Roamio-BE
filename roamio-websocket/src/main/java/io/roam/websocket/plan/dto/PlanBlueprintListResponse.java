package io.roam.websocket.plan.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

/**
 * 플랜 블루프린트 리스트를 웹소켓으로 전송하기 위한 DTO 클래스
 */
@Builder
public record PlanBlueprintListResponse(
    LocalDateTime startDate,
    LocalDateTime endDate,
    List<PlanBlueprintWebSocketResponse> blueprints
) {
} 