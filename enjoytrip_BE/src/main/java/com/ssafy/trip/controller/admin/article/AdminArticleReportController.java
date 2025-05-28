package com.ssafy.trip.controller.admin.article;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.trip.service.admin.article.AdminArticleReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/report")
@Tag(name = "AdminArticleInteractionController", description = "관리자 신고 관련 기능")
public class AdminArticleReportController {
	
	private final AdminArticleReportService reportService;

	@Operation(description = "게시글 신고 목록 조회")
	@GetMapping("/articles")
	public ResponseEntity<?> reportArticleList(AdminArticleRequest.ReportSearchCondition request) {
		return ResponseEntity.ok(reportService.reportArticleList(request));
	}
	
	@Operation(description = "댓글 신고 목록 조회")
	@GetMapping("/comments")
	public ResponseEntity<?> commentsArticleList(AdminArticleRequest.ReportSearchCondition request) {
		return ResponseEntity.ok(reportService.reportCommentList(request));
	}
	
	@Operation(description = "게시글 신고 상태 승인/거부")
	@PutMapping("/articles/{report-id}")
	public ResponseEntity<?> processArticleReport(@PathVariable("report-id") Long reportId, @RequestBody AdminArticleRequest.ReportRequest request) {
		reportService.processArticleReport(reportId, request);
		return ResponseEntity.ok("신고 처리 완료");
	}
	
	@Operation(description = "댓글 신고 상태 승인/거부")
	@PutMapping("/comments/{report-id}")
	public ResponseEntity<?> processCommentReport(@PathVariable("report-id") Long reportId, @RequestBody AdminArticleRequest.ReportRequest request) {
		reportService.processCommentReport(reportId, request);
		return ResponseEntity.ok("신고 처리 완료");
	}
	
}

