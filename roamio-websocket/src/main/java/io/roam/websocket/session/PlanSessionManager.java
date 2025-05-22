package io.roam.websocket.session;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PlanSessionManager {
    /**
     * planId 별 세션 관리
     */
    private final Map<String, Set<WebSocketSession>> planIdSessions;

    public PlanSessionManager() {
        this.planIdSessions = new ConcurrentHashMap<>();
    }

    /**
     * 특정 Plan에 유저 세션 추가
     * @param planId 플랜 ID
     * @param session 웹소켓 세션
     */
    public void addSession(String planId, WebSocketSession session) {
        planIdSessions.computeIfAbsent(planId, k -> ConcurrentHashMap.newKeySet()).add(session);
        log.info("유저 참여 - PlanId: {}, clientId: {}", planId, session.getAttributes().get("clientId"));
    }

    /**
     * 특정 Plan에서 유저 세션 제거
     * @param planId 플랜 ID
     * @param session 웹소켓 세션
     */
    public void removeSession(String planId, WebSocketSession session) {
        planIdSessions.computeIfAbsent(planId, k -> ConcurrentHashMap.newKeySet()).remove(session);
        log.info("유저 퇴장 - PlanId: {}, clientId: {}", planId, session.getAttributes().get("clientId"));
    }

    /**
     * 특정 Plan에 참여한 모든 유저 세션 조회
     * @param planId 플랜 ID
     * @return 참여한 모든 유저 세션
     */
    public Set<WebSocketSession> getSessions(String planId) {
        return planIdSessions.getOrDefault(planId, ConcurrentHashMap.newKeySet());
    }
}
