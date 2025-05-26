package io.roam.websocket.plan.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.roam.plan.entity.PlanBlueprint;
import io.roam.plan.repository.PlanBlueprintRepository;
import io.roam.plan.repository.PlanRepository;
import io.roam.plan.entity.Plan;
import io.roam.user.entity.User;
import io.roam.websocket.plan.dto.ConnectedUserResponse;
import io.roam.websocket.plan.dto.ConnectedUsersListResponse;
import io.roam.websocket.plan.dto.PlanBlueprintWebSocketResponse;
import io.roam.websocket.plan.dto.PlanBlueprintListResponse;
import io.roam.websocket.plan.dto.request.PlanBlueprintRequest;
import io.roam.websocket.plan.service.PlanSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanSessionService planSessionService;
    private final PlanBlueprintRepository planBlueprintRepository;
    private final PlanRepository planRepository;
    
    public ConnectedUsersListResponse getConnectedUsers(String planId) {
            
        // 현재 플랜에 접속 중인 모든 세션 가져오기
        List<ConnectedUserResponse> connectedUsers = planSessionService.getSessions(planId).stream()
            .map(s -> {
                User user = (User) s.getAttributes().get("user");
                return ConnectedUserResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
            })
            .toList();
        
        // 접속 중인 사용자 목록 응답 생성
        return ConnectedUsersListResponse.builder()
            .users(connectedUsers)
            .build();
    }
    
    /**
     * Blueprint를 추가하거나 수정합니다.
     * @param planId 플랜 ID
     * @param request Blueprint 요청 데이터
     * @return 웹소켓용 Blueprint 응답 데이터
     */
    public PlanBlueprintWebSocketResponse addOrUpdateBlueprint(Long planId, PlanBlueprintRequest request) {
        try {
            Plan planProxy = planRepository.getReference(planId);
            PlanBlueprint blueprint;
            String action;

            if (request.id() != null) {
                // 수정
                blueprint = planBlueprintRepository.findByIdAndPlanId(request.id(), planId)
                    .orElseThrow(() -> new RuntimeException("PlanBlueprint not found"));
                blueprint.setDay(request.day());
                blueprint.setPosition(request.position());
                blueprint.setPlaceId(request.placeId());
                blueprint.setLatitude(request.latitude());
                blueprint.setLongitude(request.longitude());
                blueprint = planBlueprintRepository.save(blueprint);
                action = "UPDATE";
            } else {
                // 생성
                blueprint = planBlueprintRepository.save(PlanBlueprint.builder()
                    .plan(planProxy)
                    .day(request.day())
                    .placeId(request.placeId())
                    .position(request.position())
                    .latitude(request.latitude())
                    .longitude(request.longitude())
                    .build());
                action = "CREATE";
            }

            return PlanBlueprintWebSocketResponse.builder()
                .id(blueprint.getId())
                .planId(planId)
                .day(blueprint.getDay())
                .position(blueprint.getPosition())
                .placeId(blueprint.getPlaceId())
                .latitude(blueprint.getLatitude())
                .longitude(blueprint.getLongitude())
                .startTime(blueprint.getStartTime())
                .endTime(blueprint.getEndTime())
                .memo(blueprint.getMemo())
                .action(action)
                .build();
        } catch (Exception e) {
            log.error("Error adding/updating blueprint: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Blueprint를 삭제합니다.
     * @param planId 플랜 ID
     * @param blueprintId Blueprint ID
     * @return 웹소켓용 Blueprint 응답 데이터
     */
    public PlanBlueprintWebSocketResponse deleteBlueprint(Long planId, Long blueprintId) {
        try {
            PlanBlueprint blueprint = planBlueprintRepository.findByIdAndPlanId(blueprintId, planId)
                .orElseThrow(() -> new RuntimeException("PlanBlueprint not found"));
            
            // 삭제 전에 응답 데이터 생성
            PlanBlueprintWebSocketResponse response = PlanBlueprintWebSocketResponse.builder()
                .id(blueprint.getId())
                .planId(planId)
                .day(blueprint.getDay())
                .position(blueprint.getPosition())
                .placeId(blueprint.getPlaceId())
                .latitude(blueprint.getLatitude())
                .longitude(blueprint.getLongitude())
                .startTime(blueprint.getStartTime())
                .endTime(blueprint.getEndTime())
                .memo(blueprint.getMemo())
                .action("DELETE")
                .build();
            
            // 삭제
            planBlueprintRepository.delete(blueprint);
            
            return response;
        } catch (Exception e) {
            log.error("Error deleting blueprint: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * 플랜의 모든 블루프린트 리스트를 조회합니다.
     * @param planId 플랜 ID
     * @return 블루프린트 리스트 응답
     */
    public PlanBlueprintListResponse getPlanBlueprintList(Long planId) {
        List<PlanBlueprint> blueprints = planBlueprintRepository.findByPlanIdOrderByDayAscPositionAsc(planId);
        
        List<PlanBlueprintWebSocketResponse> blueprintResponses = blueprints.stream()
            .map(blueprint -> PlanBlueprintWebSocketResponse.builder()
                .id(blueprint.getId())
                .planId(planId)
                .day(blueprint.getDay())
                .position(blueprint.getPosition())
                .placeId(blueprint.getPlaceId())
                .latitude(blueprint.getLatitude())
                .longitude(blueprint.getLongitude())
                .startTime(blueprint.getStartTime())
                .endTime(blueprint.getEndTime())
                .memo(blueprint.getMemo())
                .action("INIT")
                .build())
            .toList();
        
        return new PlanBlueprintListResponse(blueprintResponses);
    }
}
