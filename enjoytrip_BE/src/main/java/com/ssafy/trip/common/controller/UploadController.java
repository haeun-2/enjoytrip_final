package com.ssafy.trip.common.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.trip.external.aws.S3Service;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/upload")
@Tag(name = "Upload Controller", description = "이미지 업로드 경로 반환")
public class UploadController {
	
	private final S3Service s3Service;
	
	@GetMapping("/presign-url")
	public ResponseEntity<Map<String, String>> getPresignedUrl(@RequestParam String filename) {
		String url = s3Service.getPresignedUrl("uploads", filename);
		return ResponseEntity.ok(Map.of("url", url));
	}

}
