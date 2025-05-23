package io.roam.websocket.plan.service;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.roam.websocket.plan.domain.PlanMessage;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
public class PlanSessionService {
    /**
     * planId 별 세션 관리
     */
    private final Map<String, Set<WebSocketSession>> planIdSessions;

    private final ObjectMapper objectMapper;

    public PlanSessionService(@Autowired ObjectMapper objectMapper) {
        this.planIdSessions = new ConcurrentHashMap<>();
        this.objectMapper = objectMapper;
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
        return planIdSessions.computeIfAbsent(planId, k -> ConcurrentHashMap.newKeySet());
    }

    public void sendMessageToGroup(String planId, PlanMessage<?> planMessage) {
        log.info("planId: {}", planId);
        Set<WebSocketSession> group = getSessions(planId);

        if (group.isEmpty()) {
            log.warn("No sessions found for planId: {}", planId);
            return;
        }
        
        try {
            // 메시지를 JSON 문자열로 직렬화
            String messageJson = objectMapper.writeValueAsString(planMessage);
            TextMessage textMessage = new TextMessage(messageJson);
            
            // 모든 세션을 Flux로 변환하여 병렬 처리
            Flux.fromIterable(group)
                .parallel()  // 병렬 처리 활성화
                .runOn(Schedulers.parallel())  // 병렬 스케줄러 사용
                .flatMap(session -> sendMessageToSession(session, textMessage))
                .sequential()  // 결과를 다시 순차적으로 모음
                .doOnComplete(() -> log.debug("Message sent to all {} sessions in plan {}", group.size(), planId))
                .doOnError(e -> log.error("Error sending message to sessions in plan {}: {}", planId, e.getMessage(), e))
                .subscribe();  // 실행 시작
                
        } catch (Exception e) {
            log.error("Failed to serialize message for plan {}: {}", planId, e.getMessage(), e);
        }
    }

    private Mono<Boolean> sendMessageToSession(WebSocketSession session, TextMessage message) {
        return Mono.fromCallable(() -> {
            try {
                if (session.isOpen()) {
                    session.sendMessage(message);
                    return true;
                } else {
                    log.debug("Session {} is closed, skipping message", session.getId());
                    return false;
                }
            } catch (IOException e) {
                log.warn("Failed to send message to session {}: {}", session.getId(), e.getMessage());
                throw e;  // Mono.error로 전환되도록 예외 다시 던지기
            }
        }).onErrorResume(e -> {
            // 에러가 발생해도 전체 스트림이 중단되지 않도록 처리
            log.error("Error sending message to session: {}", e.getMessage());
            return Mono.just(false);
        }).subscribeOn(Schedulers.boundedElastic());  // I/O 작업에 적합한 스케줄러 사용
    }
}
