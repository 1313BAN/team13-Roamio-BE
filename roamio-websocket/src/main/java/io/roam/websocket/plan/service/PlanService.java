package io.roam.websocket.plan.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
import io.roam.websocket.plan.dto.PlanDateUpdateResponse;
import io.roam.websocket.plan.dto.request.PlanBlueprintRequest;
import io.roam.websocket.plan.dto.request.PlanDateUpdateRequest;
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
    public PlanBlueprintWebSocketResponse addOrUpdateBlueprint(Long planId, User user, PlanBlueprintRequest request) {
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
                    .user(user)
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
                .userId(user.getUserId())
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
        Plan plan = planRepository.findById(planId)
            .orElseThrow(() -> new RuntimeException("Plan not found"));
        List<PlanBlueprint> blueprints = planBlueprintRepository.findByPlanIdOrderByDayAscPositionAsc(planId);
        
        List<PlanBlueprintWebSocketResponse> blueprintResponses = blueprints.stream()
            .map(blueprint -> PlanBlueprintWebSocketResponse.builder()
                .id(blueprint.getId())
                .planId(planId)
                .userId(blueprint.getUser() != null ? blueprint.getUser().getUserId() : null)
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
        
        return PlanBlueprintListResponse.builder()
            .startDate(plan.getStartDate())
            .endDate(plan.getEndDate())
            .blueprints(blueprintResponses)
            .build();
    }

    /**
     * 플랜 날짜를 업데이트합니다.
     * @param planId 플랜 ID
     * @param request 날짜 업데이트 요청
     * @return 날짜 업데이트 응답
     */
    public PlanDateUpdateResponse updatePlanDates(Long planId, PlanDateUpdateRequest request) {
        try {
            // 플랜 조회
            Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));
            
            // 블루프린트의 최대 day 조회
            Integer maxDay = planBlueprintRepository.findMaxDayByPlanId(planId);
            
            // 새로운 날짜 기간 계산
            long newDayCount = ChronoUnit.DAYS.between(
                request.startDate().toLocalDate(), 
                request.endDate().toLocalDate()
            ) + 1; // 시작일 포함
            
            // 블루프린트가 존재하는 경우 날짜 제약 확인
            if (maxDay != null) {
                long requiredDayCount = maxDay;
                
                if (newDayCount < requiredDayCount) {
                    return PlanDateUpdateResponse.builder()
                        .planId(planId)
                        .startDate(plan.getStartDate())
                        .endDate(plan.getEndDate())
                        .action("FAILED")
                        .build();
                }
            }
            
            // 날짜 업데이트
            plan.setStartDate(request.startDate());
            plan.setEndDate(request.endDate());
            planRepository.save(plan);
            
            log.info("Plan dates updated for plan ID: {}, new period: {} to {}", 
                planId, request.startDate(), request.endDate());
            
            return PlanDateUpdateResponse.builder()
                .planId(planId)
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .action("SUCCESS")
                .build();
                
        } catch (Exception e) {
            log.error("Error updating plan dates for plan ID: {}", planId, e);
            
            return PlanDateUpdateResponse.builder()
                .planId(planId)
                .startDate(null)
                .endDate(null)
                .action("ERROR")
                .build();
        }
    }
}
