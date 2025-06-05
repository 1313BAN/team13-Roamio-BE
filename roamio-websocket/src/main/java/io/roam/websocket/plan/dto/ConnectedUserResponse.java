package io.roam.websocket.plan.dto;

import lombok.Builder;

/**
 * 접속 중인 사용자 정보를 담는 DTO 클래스
 */
@Builder
public record ConnectedUserResponse(
    String userId,
    String email,
    String name,
    String profileImageUrl
) {
} 