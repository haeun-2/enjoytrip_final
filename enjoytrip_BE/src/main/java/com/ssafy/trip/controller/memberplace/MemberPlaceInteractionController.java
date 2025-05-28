package com.ssafy.trip.controller.memberplace;

import com.ssafy.trip.security.auth.util.AuthenticationUserUtil;
import com.ssafy.trip.service.memberplace.MemberPlaceInteractionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hotplaces")
@RequiredArgsConstructor
@Tag(name = "MemberPlaceLikeController", description = "핫플레이스 좋아요 관련 기능")
public class MemberPlaceInteractionController {
    private final MemberPlaceInteractionService memberPlaceInteractionService;

    @PostMapping("/like")
    @Operation(summary = "핫플레이스 좋아요 등록")
    public ResponseEntity<String> like(@RequestParam Long memberPlaceId, Authentication authentication) {
        Long memberId = AuthenticationUserUtil.getUserId(authentication);
        memberPlaceInteractionService.like(memberPlaceId, memberId);
        return ResponseEntity.ok().body("좋아요 등록 완료");
    }

    @DeleteMapping("/like")
    @Operation(summary = "핫플레이스 좋아요 취소")
    public ResponseEntity<String> unlike(@RequestParam Long memberPlaceId, Authentication authentication) {
        Long memberId = AuthenticationUserUtil.getUserId(authentication);
        memberPlaceInteractionService.unlike(memberPlaceId, memberId);
        return ResponseEntity.ok().body("좋아요 취소 완료");
    }
}
