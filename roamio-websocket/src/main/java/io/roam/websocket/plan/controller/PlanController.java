package io.roam.websocket.plan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import io.roam.websocket.plan.domain.PlanMessage;
import io.roam.websocket.plan.domain.PlanMessageType;
import io.roam.websocket.plan.dto.ConnectedUserResponse;
import io.roam.websocket.plan.dto.ConnectedUsersListResponse;
import io.roam.websocket.plan.dto.PlanBlueprintWebSocketResponse;
import io.roam.websocket.plan.dto.PlanCursorResponse;
import io.roam.websocket.plan.dto.request.PlanBlueprintRequest;
import io.roam.websocket.plan.service.PlanSessionService;
import io.roam.websocket.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import io.roam.websocket.plan.domain.CursorPos;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;
    private final PlanSessionService planSessionService;
    private final ObjectMapper objectMapper;

    public void dispatch(WebSocketSession session, PlanMessage<?> planMessage) {
        switch (planMessage.getType()) {
            case POS -> sendPosition(session, planMessage.getPayload());
            case BLUEPRINT -> sendBlueprint(session, planMessage.getPayload());
            case REMOVE_BLUEPRINT -> sendRemoveBlueprint(session, planMessage.getPayload());
            default -> log.info("Unknown message type: {}", planMessage.getType());
        }
    }

    /**
     * Blueprint 추가/수정 요청을 처리합니다.
     * @param session 웹소켓 세션
     * @param payload Blueprint 요청 데이터
     */
    private void sendBlueprint(WebSocketSession session, Object payload) {
        try {
            String planId = (String) session.getAttributes().get("planId");
            
            // payload를 PlanBlueprintRequest로 변환
            PlanBlueprintRequest request;
            if (payload instanceof String) {
                request = objectMapper.readValue((String) payload, PlanBlueprintRequest.class);
            } else {
                request = objectMapper.convertValue(payload, PlanBlueprintRequest.class);
            }
            
            // Blueprint 추가/수정 처리
            PlanBlueprintWebSocketResponse response = planService.addOrUpdateBlueprint(Long.valueOf(planId), request);
            
            // 같은 플랜에 접속한 모든 사용자에게 Blueprint 변경사항 전송
            planSessionService.sendMessageToGroup(planId, 
                PlanMessage.of(PlanMessageType.BLUEPRINT, response));
                
            log.info("Blueprint {} completed for plan: {}, blueprint ID: {}", 
                response.action(), planId, response.id());
        } catch (Exception e) {
            log.error("Error processing blueprint request: {}", e.getMessage(), e);
        }
    }

    /**
     * Blueprint 삭제 요청을 처리합니다.
     * @param session 웹소켓 세션
     * @param payload Blueprint 삭제 요청 데이터 (blueprintId)
     */
    private void sendRemoveBlueprint(WebSocketSession session, Object payload) {
        try {
            String planId = (String) session.getAttributes().get("planId");
            
            // payload에서 blueprintId 추출
            Long blueprintId;
            if (payload instanceof String) {
                blueprintId = Long.valueOf((String) payload);
            } else if (payload instanceof Number) {
                blueprintId = ((Number) payload).longValue();
            } else if (payload instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> payloadMap = (Map<String, Object>) payload;
                blueprintId = Long.valueOf(payloadMap.get("blueprintId").toString());
            } else {
                throw new IllegalArgumentException("Invalid payload format for blueprint deletion");
            }
            
            // Blueprint 삭제 처리
            PlanBlueprintWebSocketResponse response = planService.deleteBlueprint(Long.valueOf(planId), blueprintId);
            
            // 같은 플랜에 접속한 모든 사용자에게 Blueprint 삭제 알림 전송
            planSessionService.sendMessageToGroup(planId, 
                PlanMessage.of(PlanMessageType.REMOVE_BLUEPRINT, response));
                
            log.info("Blueprint deleted for plan: {}, blueprint ID: {}", planId, blueprintId);
        } catch (Exception e) {
            log.error("Error processing blueprint deletion: {}", e.getMessage(), e);
        }
    }

    /**
     * 접속 중인 사용자 목록을 요청한 사용자에게 전송합니다.
     * @param session 요청한 사용자의 웹소켓 세션
     */
    public void sendUserListToSession(WebSocketSession session) {
        try {
            String planId = (String) session.getAttributes().get("planId");
            
            ConnectedUsersListResponse response = planService.getConnectedUsers(planId);

            // 요청한 사용자에게만 접속 중인 사용자 목록 전송
            planSessionService.sendMessageToSession(session, 
                PlanMessage.of(PlanMessageType.USER_LIST, response));
                
            log.info("Sent user list to session: {}, users count: {}", 
                session.getId(), response.users().size());
        } catch (Exception e) {
            log.error("Error sending user list: {}", e.getMessage(), e);
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

    
