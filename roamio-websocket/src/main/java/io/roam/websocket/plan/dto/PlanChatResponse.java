package io.roam.websocket.plan.dto;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record PlanChatResponse(
    String userId,
    String userName,
    String profileImageUrl,
    String message,
    LocalDateTime timestamp
) {
} 