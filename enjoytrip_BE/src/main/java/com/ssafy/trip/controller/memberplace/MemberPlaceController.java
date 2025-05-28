package com.ssafy.trip.controller.memberplace;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.trip.common.controller.Page;
import com.ssafy.trip.service.memberplace.MemberPlaceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/hotplaces")
@RequiredArgsConstructor
@Tag(name = "MemberPlaceController", description = "회원 등록 핫플레이스 조회")
public class MemberPlaceController {

	private final MemberPlaceService mpService;
	
	@Operation(summary = "전체 핫플레이스 조회")
	@GetMapping
	public ResponseEntity<Page<MemberPlaceResponse.Info>> list(@ParameterObject MemberPlaceRequest.SearchCondition request, Authentication authentication) {
		System.out.println(request);
		if(authentication == null) return ResponseEntity.ok(mpService.selectAll(request, null)); 
		else return ResponseEntity.ok(mpService.selectAll(request, authentication.getName())); 
	}

	@Operation(summary = "핫플레이스 등록")
	@PostMapping
	public ResponseEntity<String> create(@RequestBody MemberPlaceRequest.Data request, Authentication authentication) {
		System.out.println(request);
		mpService.insert(request, authentication.getName());
		return new ResponseEntity<>("핫플레이스 등록 완료", HttpStatus.CREATED);
	}
	
	@Operation(summary = "핫플레이스 수정")
	@PutMapping("/{hotplace-id}")
	public ResponseEntity<String> edit(@PathVariable("hotplace-id") Long hotplaceId, @RequestBody MemberPlaceRequest.Data request, Authentication authentication) {
		mpService.update(request, hotplaceId, authentication.getName());
		return ResponseEntity.ok("핫플레이스 수정 완료");
	}
	
	@Operation(summary = "핫플레이스 상세 조회")
	@GetMapping("/{hotplace-id}")
	public ResponseEntity<MemberPlaceResponse.Info> detail(@PathVariable("hotplace-id") Long hotplaceId, Authentication authentication) {
		MemberPlaceResponse.Info response = mpService.select(hotplaceId);
		return ResponseEntity.ok(response);
	}
	
	@Operation(summary = "핫플레이스 삭제")
	@DeleteMapping("/{hotplace-id}")
	public ResponseEntity<?> delete(@PathVariable("hotplace-id") Long hotplaceId, Authentication authentication) {
		mpService.delete(hotplaceId, authentication.getName());
		return ResponseEntity.ok("핫플레이스 삭제 성공");
	}

	
}
