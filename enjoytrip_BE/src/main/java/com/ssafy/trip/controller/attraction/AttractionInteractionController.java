package com.ssafy.trip.controller.attraction;

import com.ssafy.trip.security.auth.util.AuthenticationUserUtil;
import com.ssafy.trip.service.attraction.AttractionInteractionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attractions")
@RequiredArgsConstructor
@Tag(name = "AttractionLikeController", description = "관광지 찜 관련 기능")
public class AttractionInteractionController {
    private final AttractionInteractionService attractionInteractionService;

    @PostMapping("/like")
    public ResponseEntity<String> like(@RequestParam long attractionId, Authentication authentication) {
        long memberId = AuthenticationUserUtil.getUserId(authentication);
        attractionInteractionService.like(attractionId, memberId);
        return ResponseEntity.ok().body("관광지 좋아요 등록 완료");
    }

    @DeleteMapping("/unlike")
    public ResponseEntity<String> unlike(@RequestParam long attractionId, Authentication authentication) {
        long memberId = AuthenticationUserUtil.getUserId(authentication);
        attractionInteractionService.unlike(attractionId, memberId);
        return ResponseEntity.ok().body("관광지 좋아요 등록 취소");
    }
}
