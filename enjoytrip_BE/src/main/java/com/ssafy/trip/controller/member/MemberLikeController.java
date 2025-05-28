package com.ssafy.trip.controller.member;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.trip.common.controller.PageRequest;
import com.ssafy.trip.security.auth.util.AuthenticationUserUtil;
import com.ssafy.trip.service.MemberLikeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "MemberLikeController", description = "좋아요 한 글 조회 기능")
public class MemberLikeController {

    private final MemberLikeService memberLikeService;

    @Operation(description = "좋아요 한 게시글 조회")
    @GetMapping("/{member-id}/like/articles")
    public ResponseEntity<?> getLikedPosts(@PathVariable("member-id") Long memberId, @ParameterObject PageRequest request, Authentication authentication) {
        validateUserAccess(memberId, authentication);
        return ResponseEntity.ok(memberLikeService.getLikedArticles(memberId, request));
    }

    @Operation(description = "좋아요 한 관광지 조회")
    @GetMapping("/{member-id}/like/attractions")
    public ResponseEntity<?> getLikedAttractions(@PathVariable("member-id") Long memberId, @ParameterObject PageRequest request, Authentication authentication) {
        validateUserAccess(memberId, authentication);
        return ResponseEntity.ok(memberLikeService.getLikedAttractions(memberId, request));
    }

    @Operation(description = "좋아요 한 핫플 조회")
    @GetMapping("/{member-id}/like/hotplaces")
    public ResponseEntity<?> getLikedHotPlaces(@PathVariable("member-id") Long memberId, @ParameterObject PageRequest request, Authentication authentication) {
        validateUserAccess(memberId, authentication);
        return ResponseEntity.ok(memberLikeService.getLikedMemberPlaces(memberId, request));
    }

    @Operation(description = "좋아요 한 여행계획 조회")
    @GetMapping("/{member-id}/like/plans")
    public ResponseEntity<?> getLikedPlans(@PathVariable("member-id") Long memberId, @ParameterObject PageRequest request, Authentication authentication) {
        validateUserAccess(memberId, authentication);
        return ResponseEntity.ok(memberLikeService.getLikedTripPlans(memberId, request));
    }

    private void validateUserAccess(Long memberId, Authentication authentication) {
        Long currentUserId = AuthenticationUserUtil.getUserId(authentication);
        if (!memberId.equals(currentUserId)) {
            throw new AuthorizationDeniedException("본인의 좋아요 목록만 조회할 수 있습니다.");
        }
    }
}
