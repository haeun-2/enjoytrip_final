package com.ssafy.trip.controller.tripplan;

import com.ssafy.trip.common.controller.Page;
import com.ssafy.trip.common.controller.PageRequest;
import com.ssafy.trip.security.auth.util.AuthenticationUserUtil;
import com.ssafy.trip.service.tripplan.TripPlanService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final TripPlanService tripPlanService;


    @GetMapping
    @Operation(summary = "여행 계획 리스트 조회")
    public ResponseEntity<Page<PlanResponse.Summary>> getPlanList(
            @ParameterObject PlanRequest.SearchCondition condition,
            @ModelAttribute PageRequest pageRequest,
            Authentication authentication) {
        var response = tripPlanService.getAllTripPlans(condition, pageRequest, authentication);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("mine")
    @Operation(summary = "나의 계획 조회(status)")
    public ResponseEntity<Page<PlanResponse.Summary>> getPlanList(
            @RequestParam(required = false) String status,
            @ModelAttribute PageRequest pageRequest,
            Authentication authentication) {
        long userId = AuthenticationUserUtil.getUserId(authentication);
        var response = tripPlanService.getPlansWithStatus(status, pageRequest, userId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{plan-id}")
    @Operation(summary = "여행 계획 상세 조회")
    public ResponseEntity<PlanResponse.Info> geDetail(@PathVariable("plan-id") Long planId) {
        var response = tripPlanService.getDetail(planId);
    	return ResponseEntity.ok().body(response);
    }

    @PostMapping
    @Operation(summary = "여행 계획 등록")
    public ResponseEntity<PlanResponse.Summary> create(
            @RequestBody PlanRequest.Create request,
            Authentication authentication) {
        Long memberId = AuthenticationUserUtil.getUserId(authentication);
    	var response = tripPlanService.create(request, memberId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/{plan-id}")
    @Operation(summary = "여행 상세 계획 등록")
    public ResponseEntity<String> createDetail(
            @PathVariable("plan-id") Long planId,
            @RequestBody PlanRequest.CreateDetail request,
            Authentication authentication) {
    	tripPlanService.createDetail(request, planId);
        return ResponseEntity.ok().body("여행계획 등록 완료");
    }

    @PutMapping("/{plan-id}")
    @Operation(summary = "여행 계획 수정")
    public ResponseEntity<String> update(
            @PathVariable("plan-id") Long planId,
            @RequestBody PlanRequest.CreateDetail request,
            Authentication authentication) {
        Long memberId = AuthenticationUserUtil.getUserId(authentication);
        tripPlanService.update(request, planId, memberId);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{plan-id}")
    @Operation(summary = "여행 계획 삭제")
    public ResponseEntity<String> delete(
            @PathVariable("plan-id") Long planId,
            Authentication authentication) {
        Long memberId = AuthenticationUserUtil.getUserId(authentication);
        tripPlanService.delete(planId, memberId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("trending")
    @Operation(summary = "인기 여행 계획 조회")
    public ResponseEntity<PlanResponse.Info> getTrendingPlan() {
        var response = tripPlanService.getTrendingDetail();
        return ResponseEntity.ok().body(response);
    }
    
    @GetMapping("optimize-route")
    @Operation(summary = "여행 최적 경로 찾기")
    public ResponseEntity<PlanResponse.Info> optimizeRoute() {
        return ResponseEntity.ok().build();
    }
}
