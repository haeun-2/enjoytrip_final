package com.ssafy.trip.controller.admin.member;

import com.ssafy.trip.service.admin.member.AdminStatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/stat")
@RequiredArgsConstructor
@Tag(name = "AdminStatController", description = "관리자 통계")
public class AdminStatController {
    private final AdminStatService adminStatService;

    @GetMapping("/count")
    public ResponseEntity<AdminStatResponse.Count> count() {
        var count = adminStatService.getCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/member")
    public ResponseEntity<AdminStatResponse.MemberCount> member() {
        var response = adminStatService.getMemberCount();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/monthly")
    public ResponseEntity<AdminStatResponse.MonthlyStat> monthly() {
        var response = adminStatService.getMonthlyStat();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category")
    public ResponseEntity<AdminStatResponse.CategoryCount> category() {
        var response = adminStatService.getCategoryCount();
        return ResponseEntity.ok(response);
    }
}
