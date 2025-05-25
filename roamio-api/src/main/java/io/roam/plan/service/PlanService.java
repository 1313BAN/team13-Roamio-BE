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
import io.roam.plan.dto.request.PlanCreateRequest;
import io.roam.plan.dto.response.PlanCreateResponse;
import io.roam.plan.dto.response.PlanListResponse;
import io.roam.plan.dto.response.PlanResponse;
import io.roam.plan.entity.Plan;
import io.roam.plan.entity.PlanCollaborator;
import io.roam.plan.repository.PlanCollaboratorRepository;
import io.roam.plan.repository.PlanRepository;
import io.roam.user.entity.User;
import io.roam.user.repository.UserRepository;
import io.roam.user.type.UserErrorCode;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class PlanService {
    private final PlanRepository planRepository;
    private final PlanCollaboratorRepository planCollaboratorRepository;
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
}
