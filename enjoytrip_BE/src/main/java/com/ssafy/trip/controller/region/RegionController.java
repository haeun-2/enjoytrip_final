package com.ssafy.trip.controller.region;

import com.ssafy.trip.domain.attraction.region.Gugun;
import com.ssafy.trip.domain.attraction.region.Sido;
import com.ssafy.trip.service.attraction.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/region")
@RequiredArgsConstructor
@Tag(name = "RegionController", description = "지역 조회")
public class RegionController {
    private final RegionService regionService;

    @GetMapping("/sido")
    @Operation(summary = "시도 목록 조회")
    public ResponseEntity<List<Sido>> getSido() {
        List<Sido> response = regionService.getAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/gugun")
    @Operation(summary = "구군 목록 조회")
    public ResponseEntity<List<Gugun>> getGugun(@RequestParam int sidoId) {
        List<Gugun> response = regionService.getAllGugun(sidoId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("v2/gugun")
    @Operation(summary = "구군 목록 조회")
    public ResponseEntity<List<Gugun>> getV2Gugun(@RequestParam Long sidoCode) {
        List<Gugun> response = regionService.getAllGugunByCode(sidoCode);
        return ResponseEntity.ok(response);
    }
}