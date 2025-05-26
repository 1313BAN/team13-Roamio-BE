package io.roam.websocket.plan.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;

/**
 * 웹소켓으로 전송할 PlanBlueprint 정보를 담는 DTO 클래스
 */
@Builder
public record PlanBlueprintWebSocketResponse(
    Long id,
    Long planId,
    Integer day,
    BigDecimal position,
    String placeId,
    Double latitude,
    Double longitude,
    LocalDateTime startTime,
    LocalDateTime endTime,
    String memo,
    String action // "CREATE", "UPDATE", "DELETE"
) {
} 