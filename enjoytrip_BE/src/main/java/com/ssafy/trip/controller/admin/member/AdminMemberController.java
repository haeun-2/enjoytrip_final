package com.ssafy.trip.controller.admin.member;

import com.ssafy.trip.common.controller.Page;
import com.ssafy.trip.common.controller.PageRequest;
import com.ssafy.trip.service.admin.member.AdminMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;

@RestController
@RequestMapping("/api/admin/members")
@RequiredArgsConstructor
public class AdminMemberController {
    private final AdminMemberService adminMemberService;

    @GetMapping
    public ResponseEntity<Page<AdminMemberResponse.Info>> getAllMembers(
            @ModelAttribute PageRequest pageRequest,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {
        var response = adminMemberService.getAllMembers(pageRequest, status, keyword);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("block")
    public ResponseEntity<String> blockMember(@RequestParam("memberId") Long memberId) {
        adminMemberService.blockingMember(memberId);
        return ResponseEntity.ok("사용자 정지 완료");
    }

    @DeleteMapping("block")
    public ResponseEntity<String> unblockMember(@RequestParam("memberId") Long memberId) {
        adminMemberService.unblockingMember(memberId);
        return ResponseEntity.ok("사용자 정지 해제");
    }

}
