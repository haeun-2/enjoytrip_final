package com.ssafy.trip.controller.article;

import java.net.URI;
import java.util.Map;

import com.ssafy.trip.security.auth.util.AuthenticationUserUtil;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ssafy.trip.common.controller.Page;
import com.ssafy.trip.domain.article.enums.ArticleType;
import com.ssafy.trip.service.article.ArticleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
@Tag(name = "ArticleController", description = "게시글 조회")
public class ArticleController {

	private final ArticleService articleService;

	@Operation(summary = "전체 게시글 조회")
	@GetMapping
	public ResponseEntity<Page<ArticleResponse.Info>> list(@ParameterObject ArticleRequest.SearchCondition condition, Authentication authentication) {
		return ResponseEntity.ok(articleService.selectAll(condition, condition.getMine() == null || condition.getMine() == false ? null : authentication.getName())); 
	}
	
	@Operation(summary = "QnA 게시글 조회")
	@GetMapping("/qna")
	public ResponseEntity<Page<ArticleResponse.QnaInfo>> qnaList(@ParameterObject ArticleRequest.QnaSearchCondition condition, Authentication authentication) {
		return ResponseEntity.ok(articleService.selectAllQna(condition, authentication.getName())); 
	}
	
	@Operation(summary = "게시글 등록")
	@PostMapping
	public ResponseEntity<Map<String, String>> create(@RequestBody ArticleRequest.Create request, Authentication authentication) {
		Long articleId = articleService.insert(request, authentication.getName());
		return new ResponseEntity<>(Map.of("message", "게시글 등록 완료", "id", String.valueOf(articleId)), HttpStatus.CREATED);
	}
	
	@Operation(summary = "게시글 상세 조회")
	@GetMapping("/{article-id}")
	public ResponseEntity<ArticleResponse.DetailInfo> detail(@PathVariable("article-id") Long articleId, Authentication authentication) {
		Long memberId = (authentication != null) ? AuthenticationUserUtil.getUserId(authentication) : null;
		ArticleResponse.DetailInfo response = articleService.select(articleId, memberId);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "게시글 수정")
	@PutMapping("/{article-id}")
	public ResponseEntity<String> update(@PathVariable("article-id") Long articleId, @RequestBody ArticleRequest.Update request, Authentication authentication) {
		articleService.update(request, articleId, authentication.getName());
	    return ResponseEntity.ok("게시글 수정 완료");
	}
	
	@Operation(summary = "게시글 삭제")
	@DeleteMapping("/{article-id}")
	public ResponseEntity<String> delete(@PathVariable("article-id") Long articleId, Authentication authentication) {
		articleService.delete(articleId, authentication.getName());
	    return ResponseEntity.ok("게시글 삭제 완료");
	}

}
