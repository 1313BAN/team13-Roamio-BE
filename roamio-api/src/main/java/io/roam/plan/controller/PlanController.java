package io.roam.plan.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.roam.common.response.ApiResponse;
import io.roam.plan.dto.request.PlanBlueprintRequest;
import io.roam.plan.dto.request.PlanCreateRequest;
import io.roam.plan.dto.request.PlanInviteRequest;
import io.roam.plan.dto.response.PlanBlueprintResponse;
import io.roam.plan.dto.response.PlanBlueprintsResponse;
import io.roam.plan.dto.response.PlanCollaboratorsResponse;
import io.roam.plan.dto.response.PlanCreateResponse;
import io.roam.plan.dto.response.PlanInviteResponse;
import io.roam.plan.dto.response.PlanListResponse;
import io.roam.plan.dto.response.PlanOwnerResponse;
import io.roam.plan.service.PlanService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plan")
@RequiredArgsConstructor
public class PlanController implements PlanApi {
    private final PlanService planService;

    @Override
    @GetMapping
    public ApiResponse<PlanListResponse> getPlanList() {
        return ApiResponse.of(planService.getPlanList());
    }

    @Override
    @PostMapping
    public ApiResponse<PlanCreateResponse> createPlan(@RequestBody PlanCreateRequest request) {
        return ApiResponse.of(planService.createPlan(request));
    }

    @Override
    @GetMapping("/{planId}")
    public ApiResponse<PlanBlueprintsResponse> getPlanBlueprint(@PathVariable Long planId) {
        return ApiResponse.of(planService.getPlanBlueprint(planId));
    }

    @Override
    @PostMapping("/{planId}/blueprint")
    public ApiResponse<PlanBlueprintResponse> addPlanBlueprint(@PathVariable Long planId, @RequestBody PlanBlueprintRequest request) {
        return ApiResponse.of(planService.addPlanBlueprint(planId, request));
    }

    @Override
    @DeleteMapping("/{planId}/blueprint/{blueprintId}")
    public ApiResponse<Void> deletePlanBlueprint(@PathVariable Long planId, @PathVariable Long blueprintId) {
        planService.deletePlanBlueprint(planId, blueprintId);
        return ApiResponse.of(null);
    }

    @Override
    @GetMapping("/{planId}/owner")
    public ApiResponse<PlanOwnerResponse> getPlanOwner(@PathVariable Long planId) {
        return ApiResponse.of(planService.getPlanOwner(planId));
    }

    @Override
    @GetMapping("/{planId}/collaborators")
    public ApiResponse<PlanCollaboratorsResponse> getPlanCollaborators(@PathVariable Long planId) {
        return ApiResponse.of(planService.getPlanCollaborators(planId));
    }

    @Override
    @PostMapping("/{planId}/invite")
    public ApiResponse<PlanInviteResponse> invitePlanCollaborator(@PathVariable Long planId, @RequestBody PlanInviteRequest request) {
        return ApiResponse.of(planService.invitePlanCollaborator(planId, request));
    }
}
