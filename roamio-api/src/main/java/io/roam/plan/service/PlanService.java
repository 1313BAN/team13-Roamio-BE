package io.roam.plan.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import io.roam.common.exception.DomainException;
import io.roam.jwt.entity.AuthUserDetail;
import io.roam.plan.dto.request.PlanBlueprintRequest;
import io.roam.plan.dto.request.PlanCreateRequest;
import io.roam.plan.dto.request.PlanInviteRequest;
import io.roam.plan.dto.response.PlanBlueprintResponse;
import io.roam.plan.dto.response.PlanBlueprintsResponse;
import io.roam.plan.dto.response.PlanCollaboratorResponse;
import io.roam.plan.dto.response.PlanCollaboratorsResponse;
import io.roam.plan.dto.response.PlanCreateResponse;
import io.roam.plan.dto.response.PlanInviteResponse;
import io.roam.plan.dto.response.PlanListResponse;
import io.roam.plan.dto.response.PlanOwnerResponse;
import io.roam.plan.dto.response.PlanResponse;
import io.roam.plan.entity.Plan;
import io.roam.plan.entity.PlanBlueprint;
import io.roam.plan.entity.PlanCollaborator;
import io.roam.plan.entity.PlanCollaboratorInfo;
import io.roam.plan.repository.PlanBlueprintRepository;
import io.roam.plan.repository.PlanCollaboratorRepository;
import io.roam.plan.repository.PlanRepository;
import io.roam.plan.type.PlanErrorCode;
import io.roam.user.entity.User;
import io.roam.user.repository.UserRepository;
import io.roam.user.type.UserErrorCode;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class PlanService {
    private final PlanRepository planRepository;
    private final PlanCollaboratorRepository planCollaboratorRepository;
    private final PlanBlueprintRepository planBlueprintRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
            .map(Authentication::getPrincipal)
            .filter(principal -> principal instanceof AuthUserDetail)
            .map(principal -> (AuthUserDetail) principal)
            .flatMap(auth -> userRepository.findByUserId(auth.clientId()))
            .orElseThrow(() -> new DomainException(UserErrorCode.USER_NOT_FOUND));
    }

    public PlanListResponse getPlanList() {
        User user = getCurrentUser();

        List<PlanResponse> plans = planCollaboratorRepository.findWithPlanAndOwnerByUserId(user.getId()).stream()
            .map(PlanCollaborator::getPlan)
            .map(plan -> PlanResponse.builder()
                .id(plan.getId())
                .ownerEmail(plan.getOwner().getEmail())
                .ownerName(plan.getOwner().getName())
                .title(plan.getTitle())
                .description(plan.getDescription())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .collaborators(planCollaboratorRepository.getCollaboratorListByPlanId(plan.getId()))
                .build())
            .toList();

        return PlanListResponse.builder()
            .planList(plans)
            .build();
    }

    @Transactional
    public PlanCreateResponse createPlan(PlanCreateRequest request) {
        User user = getCurrentUser();

        Plan plan = planRepository.save(Plan.builder()
            .title(request.title())
            .description(request.description())
            .startDate(request.startDate())
            .endDate(request.endDate())
            .owner(user)
            .build());

        PlanCollaborator planCollaborator = planCollaboratorRepository.save(PlanCollaborator.builder()
            .plan(plan)
            .user(user)
            .build());
            
        return PlanCreateResponse.builder()
            .id(plan.getId())
            .title(plan.getTitle())
            .description(plan.getDescription())
            .startDate(plan.getStartDate())
            .endDate(plan.getEndDate())
            .build();
    }

    public PlanBlueprintsResponse getPlanBlueprint(Long planId) {
        List<PlanBlueprint> blueprints = planBlueprintRepository.findByPlanIdOrderByDayAscPositionAsc(planId);

        List<PlanBlueprintResponse> blueprintResponses = blueprints.stream()
            .map(blueprint -> PlanBlueprintResponse.builder()
                .id(blueprint.getId())
                .planId(blueprint.getPlan().getId())
                .day(blueprint.getDay())
                .position(blueprint.getPosition())
                .placeId(blueprint.getPlaceId())
                .latitude(blueprint.getLatitude())
                .longitude(blueprint.getLongitude())
                .startTime(blueprint.getStartTime())
                .endTime(blueprint.getEndTime())
                .memo(blueprint.getMemo())
                .build())
            .toList();

        return PlanBlueprintsResponse.builder()
            .blueprints(blueprintResponses)
            .build();
    }

    public PlanBlueprintResponse addPlanBlueprint(Long planId, PlanBlueprintRequest request) {
        Plan planProxy = planRepository.getReference(planId);
        PlanBlueprint blueprint;

        if (request.id() != null) {
            blueprint = planBlueprintRepository.findByIdAndPlanId(request.id(), planId)
                .orElseThrow(() -> new RuntimeException("PlanBlueprint not found"));
            blueprint.setDay(request.day());
            blueprint.setPosition(request.position());
            blueprint = planBlueprintRepository.save(blueprint);
        } else {
            blueprint = planBlueprintRepository.save(PlanBlueprint.builder()
                .plan(planProxy)
                .day(request.day())
                .placeId(request.placeId())
                .position(request.position())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .build());
        }

        return PlanBlueprintResponse.builder()
            .id(blueprint.getId())
            .planId(blueprint.getPlan().getId())
            .day(blueprint.getDay())
            .position(blueprint.getPosition())
            .placeId(blueprint.getPlaceId())
            .latitude(blueprint.getLatitude())
            .longitude(blueprint.getLongitude())
            .build();
    }

    /**
     * 플랜 블루프린트를 삭제합니다.
     * @param planId 플랜 아이디
     * @param blueprintId 플랜 블루프린트 아이디
     * @throws RuntimeException 플랜 블루프린트가 존재하지 않는 경우
     */
    @Transactional
    public void deletePlanBlueprint(Long planId, Long blueprintId) {
        // 해당 planId와 blueprintId를 가진 blueprint가 존재하는지 확인
        PlanBlueprint blueprint = planBlueprintRepository.findByIdAndPlanId(blueprintId, planId)
            .orElseThrow(() -> new RuntimeException("PlanBlueprint not found"));
        
        // blueprint 삭제
        planBlueprintRepository.delete(blueprint);
    }

    /**
     * 계획의 소유자 정보를 조회합니다.
     * @param planId 계획 아이디
     * @return 계획 소유자 정보
     * @throws RuntimeException 계획이 존재하지 않는 경우
     */
    public PlanOwnerResponse getPlanOwner(Long planId) {
        User owner = planRepository.getReference(planId).getOwner();
        
        return PlanOwnerResponse.builder()
            .userId(owner.getUserId())
            .email(owner.getEmail())
            .name(owner.getName())
            .build();
    }

    /**
     * 계획의 협력자 목록을 조회합니다.
     * @param planId 계획 아이디
     * @return 계획 협력자 목록
     */
    public PlanCollaboratorsResponse getPlanCollaborators(Long planId) {
        List<PlanCollaboratorInfo> collaboratorInfos = planCollaboratorRepository.getCollaboratorListByPlanId(planId);
        
        List<PlanCollaboratorResponse> collaboratorResponses = collaboratorInfos.stream()
            .map(info -> PlanCollaboratorResponse.builder()
                .userId(info.getUserId())
                .email(info.getEmail())
                .name(info.getUserName())
                .profileImageUrl(info.getProfileImageUrl())
                .build())
            .toList();
        
        return PlanCollaboratorsResponse.builder()
            .collaborators(collaboratorResponses)
            .build();
    }

    /**
     * 계획에 협력자를 초대합니다.
     * @param planId 계획 아이디
     * @param request 초대 요청 정보
     * @return 초대 결과
     * @throws RuntimeException 계획이 존재하지 않거나, 권한이 없거나, 사용자를 찾을 수 없는 경우
     */
    @Transactional
    public PlanInviteResponse invitePlanCollaborator(Long planId, PlanInviteRequest request) {
        User currentUser = getCurrentUser();
        
        // 계획 조회 및 소유자 확인
        Plan plan = planRepository.findById(planId)
            .orElseThrow(() -> new DomainException(PlanErrorCode.PLAN_NOT_FOUND));
        
        if (!plan.getOwner().getUserId().equals(currentUser.getUserId())) {
            throw new DomainException(PlanErrorCode.ONLY_PLAN_OWNER_CAN_INVITE);
        }
        
        // 초대할 사용자 조회
        User invitedUser = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new DomainException(PlanErrorCode.USER_NOT_FOUND));
        
        // 이미 협력자인지 확인
        if (planCollaboratorRepository.existsByPlanIdAndUserId(planId, invitedUser.getId())) {
            throw new DomainException(PlanErrorCode.USER_ALREADY_INVITED);
        }
        
        // 협력자 추가
        planCollaboratorRepository.save(PlanCollaborator.builder()
            .plan(plan)
            .user(invitedUser)
            .build());
        
        return PlanInviteResponse.builder()
            .invitedUserEmail(invitedUser.getEmail())
            .invitedUserName(invitedUser.getName())
            .build();
    }
}

