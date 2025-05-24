package io.roam.websocket.plan.handler;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.roam.websocket.plan.controller.PlanController;
import io.roam.websocket.plan.domain.PlanMessage;
import io.roam.websocket.plan.domain.PlanMessageType;
import io.roam.websocket.plan.dto.PlanEnterResponse;
import io.roam.websocket.plan.dto.PlanLeaveResponse;
import io.roam.websocket.plan.service.PlanSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanWebSocketHandler extends TextWebSocketHandler {

    private final PlanController planController;
    private final PlanSessionService planSessionService;
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        try {
            if (session.getAttributes().get("queryParams") instanceof Map queryParams) {
                String planId = (String) queryParams.get("planId");

                if (planId == null || planId.isEmpty()) {
                    throw new IllegalArgumentException();
                }

                // TODO: 플랜 접근 권한 확인

                session.getAttributes().put("planId", planId);
                PlanEnterResponse planEnterResponse = PlanEnterResponse.builder()
                    .userId((String) session.getAttributes().get("userId"))
                    .name((String) session.getAttributes().get("name"))
                    .build();
                planSessionService.sendMessageToGroup(planId, PlanMessage.of(PlanMessageType.ENTER, planEnterResponse));
                planSessionService.addSession(planId, session);
            } else {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            log.error("Error in afterConnectionEstablished: {}", e.getMessage(), e);
            session.close(CloseStatus.PROTOCOL_ERROR);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("Received message from {}: {}", session.getId(), message.getPayload());
        PlanMessage planMessage = objectMapper.readValue(message.getPayload(), PlanMessage.class);
        planController.dispatch(session, planMessage);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("WebSocket connection closed: {} with status: {}", session.getId(), status);
        removeSession(session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket transport error: {}", exception.getMessage(), exception);
        removeSession(session);
    }

    private void removeSession(WebSocketSession session) {
        if (session.getAttributes().get("queryParams") instanceof Map queryParams) {
            String planId = (String) queryParams.get("planId");
            planSessionService.removeSession(planId, session);
            PlanLeaveResponse planLeaveResponse = PlanLeaveResponse.builder()
                .userId((String) session.getAttributes().get("userId"))
                .build();
            planSessionService.sendMessageToGroup(planId, PlanMessage.of(PlanMessageType.LEAVE, planLeaveResponse));
        }
    }
}