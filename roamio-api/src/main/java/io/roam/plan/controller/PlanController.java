package io.roam.plan.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.roam.common.response.ApiResponse;
import io.roam.plan.dto.request.PlanCreateRequest;
import io.roam.plan.dto.response.PlanCreateResponse;
import io.roam.plan.dto.response.PlanListResponse;
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
}
