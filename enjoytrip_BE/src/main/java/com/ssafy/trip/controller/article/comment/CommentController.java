package com.ssafy.trip.controller.article.comment;

import com.ssafy.trip.service.article.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/articles/{article-id}/comments")
@Tag(name = "CommentController", description = "게시글 댓글 API")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "댓글 작성")
    public ResponseEntity<?> create(@PathVariable("article-id") Long articleId,
                                           @RequestBody CommentRequest.Create request,
                                           Authentication authentication)
    {
        Long commentId = commentService.create(articleId, request, authentication.getName());
        return new ResponseEntity<>(Map.of("message", "댓글 작성 완료", "commentId", commentId), HttpStatus.CREATED);
    }

    @PutMapping("/{comment-id}")
    @Operation(summary = "댓글 수정")
    public ResponseEntity<?> update(@PathVariable("comment-id") Long commentId,
                                    @RequestBody CommentRequest.Update request,
                                    Authentication authentication)
    {
        commentService.update(commentId, request);
        return ResponseEntity.ok("댓글 수정 완료");
    }

    @DeleteMapping("/{comment-id}")
    @Operation(summary = "댓글 삭제")
    public ResponseEntity<?> delete(@PathVariable("comment-id") Long commentId,
                                    Authentication authentication) {
        commentService.delete(commentId);
        return ResponseEntity.ok("댓글 삭제 완료");
    }

    @GetMapping
    @Operation(summary = "게시글의 전체 댓글 조회")
    public ResponseEntity<List<CommentResponse.Info>> getComments(@PathVariable("article-id") Long articleId) {
        var response = commentService.get(articleId);
        return ResponseEntity.ok().body(response);
    }
}
