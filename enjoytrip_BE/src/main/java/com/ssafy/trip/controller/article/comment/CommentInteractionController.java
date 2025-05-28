package com.ssafy.trip.controller.article.comment;

import com.ssafy.trip.controller.article.ArticleRequest;
import com.ssafy.trip.security.auth.util.AuthenticationUserUtil;
import com.ssafy.trip.service.article.CommentInteractionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articles/{article-id}/comments")
@Tag(name = "CommentInteractionController", description = "게시글 댓글 신고 기능")
@RequiredArgsConstructor
public class CommentInteractionController {
    private final CommentInteractionService commentInteractionService;

    @PostMapping("/report/{comment-id}")
    @Operation(summary = "게시글 댓글 신고")
    public ResponseEntity<String> report(@PathVariable("comment-id") Long commentId,
    									@RequestBody ArticleRequest.Report request,
                                         Authentication authentication) {
        Long memberId = AuthenticationUserUtil.getUserId(authentication);
        commentInteractionService.report(commentId, memberId, request.getReason());
        return ResponseEntity.ok().body("신고 완료");
    }
}
