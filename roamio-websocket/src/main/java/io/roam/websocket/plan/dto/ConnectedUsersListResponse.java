package io.roam.websocket.plan.dto;

import java.util.List;

import lombok.Builder;

/**
 * 접속 중인 사용자 목록을 담는 응답 DTO 클래스
 */
@Builder
public record ConnectedUsersListResponse(
    List<ConnectedUserResponse> users
) {
} 