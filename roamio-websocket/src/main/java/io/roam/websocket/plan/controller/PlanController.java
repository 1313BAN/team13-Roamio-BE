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
import io.roam.websocket.plan.dto.PlanBlueprintListResponse;
import io.roam.websocket.plan.dto.PlanCursorResponse;
import io.roam.websocket.plan.dto.PlanBlueprintListResponse;
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
import io.roam.websocket.plan.domain.Viewport;

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
            
            // payload를 PlanBlueprintRequest로 변환
            PlanBlueprintRequest request;
            if (payload instanceof String) {
                request = objectMapper.readValue((String) payload, PlanBlueprintRequest.class);
            } else {
                request = objectMapper.convertValue(payload, PlanBlueprintRequest.class);
            }
            
            // Blueprint 삭제 처리
            PlanBlueprintWebSocketResponse response = planService.deleteBlueprint(Long.valueOf(planId), request.id());
            
            // 같은 플랜에 접속한 모든 사용자에게 Blueprint 삭제 알림 전송
            planSessionService.sendMessageToGroup(planId, 
                PlanMessage.of(PlanMessageType.REMOVE_BLUEPRINT, response));
                
            log.info("Blueprint deleted for plan: {}, blueprint ID: {}", planId, request.id());
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

    /**
     * 플랜 블루프린트 리스트를 요청한 사용자에게 전송합니다.
     * @param session 요청한 사용자의 웹소켓 세션
     */
    public void sendBlueprintListToSession(WebSocketSession session) {
        try {
            String planId = (String) session.getAttributes().get("planId");
            
            PlanBlueprintListResponse response = planService.getPlanBlueprintList(Long.valueOf(planId));

            // 요청한 사용자에게만 블루프린트 리스트 전송
            planSessionService.sendMessageToSession(session, 
                PlanMessage.of(PlanMessageType.BLUEPRINT_LIST, response));
                
            log.info("Sent blueprint list to session: {}, blueprints count: {}", 
                session.getId(), response.blueprints().size());
        } catch (Exception e) {
            log.error("Error sending blueprint list: {}", e.getMessage(), e);
        }
    }

    private void sendPosition(WebSocketSession session, Object payload) {
        try {
            String planId = (String) session.getAttributes().get("planId");
            
            List<CursorPos> cursorPositions = new ArrayList<>();
            Viewport viewport = null;
            
            // 새로운 구조: payload가 Map 형태로 pos와 viewport를 포함
            if (payload instanceof Map) {
                Map<String, Object> payloadMap = (Map<String, Object>) payload;
                
                // pos 데이터 처리
                Object posData = payloadMap.get("pos");
                if (posData instanceof List) {
                    List<List<Integer>> positions = (List<List<Integer>>) posData;
                    for (List<Integer> pos : positions) {
                        if (pos.size() >= 2) {
                            cursorPositions.add(CursorPos.builder()
                                .x(pos.get(0))
                                .y(pos.get(1))
                                .build());
                        }
                    }
                }
                
                // viewport 데이터 처리
                Object viewportData = payloadMap.get("viewport");
                if (viewportData instanceof Map) {
                    Map<String, Object> viewportMap = (Map<String, Object>) viewportData;
                    viewport = Viewport.builder()
                        .lat(((Number) viewportMap.get("lat")).doubleValue())
                        .lng(((Number) viewportMap.get("lng")).doubleValue())
                        .zoom(((Number) viewportMap.get("zoom")).intValue())
                        .build();
                }
            }
            
            PlanCursorResponse planCursorResponse = PlanCursorResponse.builder()
                .userId((String) session.getAttributes().get("userId"))
                .name((String) session.getAttributes().get("name"))
                .positions(cursorPositions)
                .viewport(viewport)
                .build();
                
            planSessionService.sendMessageToGroup(planId, PlanMessage.of(PlanMessageType.POS, planCursorResponse));
        } catch (Exception e) {
            log.error("Error processing position data: {}", e.getMessage(), e);
        }
    }
}

    
