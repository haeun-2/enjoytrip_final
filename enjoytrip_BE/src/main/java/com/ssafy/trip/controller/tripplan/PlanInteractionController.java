package com.ssafy.trip.controller.tripplan;

import com.ssafy.trip.security.auth.util.AuthenticationUserUtil;
import com.ssafy.trip.service.tripplan.TripPlanInteractionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
@Tag(name = "TripPlanLikeController", description = "여행계획 좋아요 관련 기능")
public class PlanInteractionController {
    private final TripPlanInteractionService tripPlanInteractionService;

    @PostMapping("/like")
    @Operation(summary = "여행 계획 좋아요")
    public ResponseEntity<String> like(@RequestParam int tripPlanId, Authentication authentication) {
        long memberId = AuthenticationUserUtil.getUserId(authentication);
        tripPlanInteractionService.like(tripPlanId, memberId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/like")
    @Operation(summary = "여행 계획 좋아요 취소")
    public ResponseEntity<String> delete(@RequestParam int tripPlanId, Authentication authentication) {
        long memberId = AuthenticationUserUtil.getUserId(authentication);
        tripPlanInteractionService.unlike(tripPlanId, memberId);
        return ResponseEntity.ok().build();
    }
}
