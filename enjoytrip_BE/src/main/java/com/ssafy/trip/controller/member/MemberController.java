package com.ssafy.trip.controller.member;

import com.ssafy.trip.security.auth.util.AuthenticationUserUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.ssafy.trip.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "Member", description = "사용자 정보")
public class MemberController {
	private final MemberService memberService;
	
	@Operation(summary = "회원 정보 조회")
    @GetMapping("/{member-id}/profile")
    public ResponseEntity<MemberResponse.Info> get(@PathVariable("member-id") Long memberId, Authentication authentication) {
		Long currentUserId = AuthenticationUserUtil.getUserId(authentication);
		MemberResponse.Info response = memberService.get(memberId, currentUserId);
		return ResponseEntity.ok(response);
    }
	
	@Operation(summary = "회원 정보 수정")
	@PutMapping("/{member-id}/profile")
	public ResponseEntity<String> update(@PathVariable("member-id") Long memberId, @RequestBody MemberRequest.Update request, Authentication authentication) {
		Long currentUserId = AuthenticationUserUtil.getUserId(authentication);
		memberService.update(memberId, request, currentUserId);
		return ResponseEntity.ok("회원 정보 수정 완료");
	}

	@Operation(summary = "비밀번호 수정")
	@PutMapping("/{member-id}/password")
	public ResponseEntity<String> updatePassword(@PathVariable("member-id") Long memberId, @RequestBody MemberRequest.UpdatePassword request, Authentication authentication) {
		Long currentUserId = AuthenticationUserUtil.getUserId(authentication);
		memberService.updatePassword(memberId, request, currentUserId);
		return ResponseEntity.ok("비밀번호 수정 완료");
	}
	
	@Operation(summary = "회원 탈퇴")
	@DeleteMapping("/{member-id}")
	public ResponseEntity<String> delete(@PathVariable("member-id") Long memberId, Authentication authentication) {
		Long currentUserId = AuthenticationUserUtil.getUserId(authentication);
		memberService.delete(memberId, currentUserId);
		return ResponseEntity.ok("회원 탈퇴 완료");
	}

}
