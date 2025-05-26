package io.roam.plan.controller;

import org.springframework.web.bind.annotation.PathVariable;

import io.roam.common.response.ApiResponse;
import io.roam.plan.dto.request.PlanBlueprintRequest;
import io.roam.plan.dto.request.PlanCreateRequest;
import io.roam.plan.dto.response.PlanBlueprintResponse;
import io.roam.plan.dto.response.PlanBlueprintsResponse;
import io.roam.plan.dto.response.PlanCreateResponse;
import io.roam.plan.dto.response.PlanListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "계획", description = "계획 관련 API")
public interface PlanApi {

    @Operation(summary = "계획 목록 조회", description = "계획 목록을 조회합니다.")
    public ApiResponse<PlanListResponse> getPlanList();

    @Operation(summary = "계획 생성", description = "계획을 생성합니다.")
    public ApiResponse<PlanCreateResponse> createPlan(@RequestBody PlanCreateRequest request);

    @Operation(summary = "계획 블루프린트 조회", description = "계획 블루프린트를 조회합니다.")
    public ApiResponse<PlanBlueprintsResponse> getPlanBlueprint(@PathVariable Long planId);

    @Operation(summary = "계획 블루프린트 추가", description = "계획 블루프린트를 추가합니다.")
    public ApiResponse<PlanBlueprintResponse> addPlanBlueprint(@PathVariable Long planId, @RequestBody PlanBlueprintRequest request);

    @Operation(summary = "계획 블루프린트 삭제", description = "계획 블루프린트를 삭제합니다.")
    public ApiResponse<Void> deletePlanBlueprint(@PathVariable Long planId, @PathVariable Long blueprintId);
}