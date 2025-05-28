package com.ssafy.trip.controller.article;

import com.ssafy.trip.security.auth.util.AuthenticationUserUtil;
import com.ssafy.trip.service.article.ArticleInteractionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
@Tag(name = "ArticleInteractionController", description = "게시글 좋아요, 신고 기능")
public class ArticleInteractionController {
    private final ArticleInteractionService articleInteractionService;

    @PostMapping("/like")
    @Operation(summary = "게시글 좋아요 등록")
    public ResponseEntity<String> likeArticle(@RequestParam Long articleId, Authentication authentication) {
        Long memberId = AuthenticationUserUtil.getUserId(authentication);
        articleInteractionService.like(articleId, memberId);
        return ResponseEntity.ok().body("좋아요 등록 완료");
    }

    @DeleteMapping("/unlike")
    @Operation(summary = "게시글 좋아요 취소")
    public ResponseEntity<String> unlikeArticle(@RequestParam Long articleId, Authentication authentication) {
        Long memberId = AuthenticationUserUtil.getUserId(authentication);
        articleInteractionService.unlike(articleId, memberId);
        return ResponseEntity.ok().body("좋아요 등록 취소");
    }

    @PostMapping("/report/{articleId}")
    @Operation(summary = "게시글 신고")
    public ResponseEntity<String> reportArticle(@PathVariable Long articleId,
                                                @RequestBody ArticleRequest.Report request,
                                                Authentication authentication) {
        Long memberId = AuthenticationUserUtil.getUserId(authentication);
        articleInteractionService.report(articleId, memberId, request.getReason());
        return ResponseEntity.ok().body("신고 완료");
    }
}
