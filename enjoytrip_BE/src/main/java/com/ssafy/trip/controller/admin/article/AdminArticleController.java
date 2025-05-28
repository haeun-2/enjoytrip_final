package com.ssafy.trip.controller.admin.article;

import java.util.Map;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.trip.common.controller.Page;
import com.ssafy.trip.service.admin.article.AdminArticleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/articles")
@Tag(name = "AdminArticleController", description = "관리자 게시글 관련 기능")
public class AdminArticleController {
	
	private final AdminArticleService adminArticleService;

	// QNA
	@Operation(summary = "전체 QnA 조회")
	@GetMapping("/qna")
	public ResponseEntity<Page<AdminArticleResponse.QnaInfo>> qnaList(@ParameterObject AdminArticleRequest.QnaSearchCondition request) {
		return ResponseEntity.ok(adminArticleService.selectAllQna(request)); 
	}
	
	@Operation(summary = "QnA 답변 등록")
	@PostMapping("/qna")
	public ResponseEntity<String> createAnswer(@RequestBody AdminArticleRequest.QnaAnswerCreate request, Authentication authentication) {
		adminArticleService.insertQnaAnswer(request, authentication.getName());
		return new ResponseEntity<>("답변 등록 완료", HttpStatus.CREATED);
	}
	
	// NOTICE
	@Operation(summary = "전체 공지사항 조회")
	@GetMapping
	public ResponseEntity<Page<AdminArticleResponse.Info>> list(@ParameterObject AdminArticleRequest.SearchCondition request) {
		return ResponseEntity.ok(adminArticleService.selectAllByAdmin(request)); 
	}
	
	@Operation(summary = "공지사항 조회")
	@GetMapping("/{article-id}")
	public ResponseEntity<AdminArticleResponse.Info> detail(@PathVariable("article-id") Long articleId) {
		return ResponseEntity.ok(adminArticleService.selectByAdmin(articleId)); 
	}
	
	@Operation(summary = "공지사항 등록")
	@PostMapping
	public ResponseEntity<Map<String, String>> create(@RequestBody AdminArticleRequest.Data request, Authentication authentication) {
		Long articleId = adminArticleService.insert(request, authentication.getName());
		return new ResponseEntity<>(Map.of("message", "공지사항 등록 완료", "id", String.valueOf(articleId)), HttpStatus.CREATED);
	}
	
	@Operation(summary = "공지사항 수정")
	@PutMapping("/{article-id}")
	public ResponseEntity<String> update(@PathVariable("article-id") Long articleId, @RequestBody AdminArticleRequest.Data request, Authentication authentication) {
		adminArticleService.update(request, articleId, authentication.getName());
	    return ResponseEntity.ok("공지사항 수정 완료");
	}
	
	@Operation(summary = "공지사항 삭제")
	@DeleteMapping("/{article-id}")
	public ResponseEntity<String> delete(@PathVariable("article-id") Long articleId) {
		adminArticleService.delete(articleId);
	    return ResponseEntity.ok("공지사항 삭제 완료");
	}
	
}
