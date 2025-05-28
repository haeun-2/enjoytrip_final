package com.ssafy.trip.controller.attraction;

import com.ssafy.trip.common.controller.Page;
import com.ssafy.trip.common.controller.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.ssafy.trip.domain.attraction.Attraction;
import com.ssafy.trip.service.attraction.AttractionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/attractions")
@RequiredArgsConstructor
@Tag(name = "AttractionController", description = "관광정보 조회")
public class AttractionController {

	private final AttractionService aService;
	
	@Operation(summary = "시도/구군/카테코리별 관광정보 조회(지도 뷰)")
	@GetMapping
	public ResponseEntity<AttractionResponse.SearchInfo> list(@ModelAttribute AttractionRequest.SearchCondition condition) {
		AttractionResponse.SearchInfo res = aService.search(condition);
		System.out.println(res);
		return ResponseEntity.ok(res);
	}

	@Operation(summary = "시도/구군/카테코리별 관광정보 페이징 조회")
	@GetMapping("/filter")
	public ResponseEntity<Page<AttractionResponse.Info>> getListWithSort(
			@ModelAttribute AttractionRequest.PagingCondition condition,
			@ModelAttribute PageRequest pageRequest) {

		var response = aService.getListWithPaging(condition, pageRequest);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "현재 좌표 중심으로 반경 distance미터 이내의 관광지 조회")
	@GetMapping("/nearby")
	public ResponseEntity<List<AttractionResponse.Info>> get(@ModelAttribute AttractionRequest.Nearby request) {
		var response = aService.getListNearby(request);
		return ResponseEntity.ok().body(response);
	}
	
	@Operation(summary = "관광정보 상세 조회")
	@GetMapping("/{attraction-id}")
	public ResponseEntity<Attraction> detail(@PathVariable("attraction-id") Long attractionId, Authentication authentication) {
		return ResponseEntity.ok(aService.select(attractionId, authentication));
	}

	@Operation(summary = "관광정보 이름으로 검색")
	@GetMapping("/search")
	public ResponseEntity<List<AttractionResponse.Name>> search(@RequestParam("keyword") String keyword) {
		List<AttractionResponse.Name> res = aService.searchKeyword(keyword);
		return ResponseEntity.ok(res);
	}
}
