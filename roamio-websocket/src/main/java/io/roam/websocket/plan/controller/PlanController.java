package io.roam.websocket.plan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.roam.websocket.plan.domain.PlanMessage;
import io.roam.websocket.plan.domain.PlanMessageType;
import io.roam.websocket.plan.service.PlanSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PlanController {

    private final PlanSessionService planSessionService;
    private final ObjectMapper objectMapper;

    public void dispatch(WebSocketSession session, PlanMessage<?> planMessage) {
        switch (planMessage.getType()) {
            case POS -> sendPosition(session, planMessage.getPayload());
            default -> log.info("Unknown message type: {}", planMessage.getType());
        }
    }

    private void sendPosition(WebSocketSession session, Object payload) {
        planSessionService.sendMessageToGroup((String) session.getAttributes().get("planId"), PlanMessage.of(PlanMessageType.POS, payload));
    }
}

    
