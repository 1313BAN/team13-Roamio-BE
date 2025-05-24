package io.roam.websocket.plan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import io.roam.websocket.plan.domain.PlanMessage;
import io.roam.websocket.plan.domain.PlanMessageType;
import io.roam.websocket.plan.dto.PlanCursorResponse;
import io.roam.websocket.plan.service.PlanSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import io.roam.websocket.plan.domain.CursorPos;

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
        try {
            String planId = (String) session.getAttributes().get("planId");
            
            List<CursorPos> cursorPositions = new ArrayList<>();
            
            // payload가 직접 [[x1, y1], [x2, y2]] 형태로 전송된 데이터 처리
            if (payload instanceof List) {
                List<List<Integer>> positions = (List<List<Integer>>) payload;
                log.info("Cursor type 1");
                for (List<Integer> pos : positions) {
                    if (pos.size() >= 2) {
                        cursorPositions.add(CursorPos.builder()
                            .x(pos.get(0))
                            .y(pos.get(1))
                            .build());
                    }
                }
            } else if (payload instanceof String) {
                // 문자열 형태로 전송된 경우 ObjectMapper로 파싱
                List<List<Integer>> positions = objectMapper.readValue((String) payload, new TypeReference<List<List<Integer>>>() {});
                log.info("Cursor type 2");
                for (List<Integer> pos : positions) {
                    if (pos.size() >= 2) {
                        cursorPositions.add(CursorPos.builder()
                            .x(pos.get(0))
                            .y(pos.get(1))
                            .build());
                    }
                }
            }
            
            PlanCursorResponse planCursorResponse = PlanCursorResponse.builder()
                .userId((String) session.getAttributes().get("userId"))
                .name((String) session.getAttributes().get("name"))
                .positions(cursorPositions)
                .build();
                
            planSessionService.sendMessageToGroup(planId, PlanMessage.of(PlanMessageType.POS, planCursorResponse));
        } catch (Exception e) {
            log.error("Error processing position data: {}", e.getMessage(), e);
        }
    }
}

    
