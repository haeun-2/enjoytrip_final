package com.ssafy.trip.controller.memberplace;

import java.time.LocalDate;
import java.util.List;

import com.ssafy.trip.domain.attraction.Category;
import com.ssafy.trip.domain.attraction.region.Gugun;
import com.ssafy.trip.domain.attraction.region.Sido;
import com.ssafy.trip.domain.memberplace.MemberPlace;
import com.ssafy.trip.domain.memberplace.MemberPlaceSearchCondition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class MemberPlaceRequest {
	
	@ToString
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Data {
		private String name;
		private Double latitude;
		private Double longitude;
		private LocalDate visitedAt;
		private String description;
		private String address;
		private String image;
		
		private Long sidoId;
		private Long gugunId;
		private Long categoryId;
		
		public MemberPlace toEntity(Sido sido, Gugun gugun, Category category) {
			return MemberPlace.builder()
			        .name(name)
			        .latitude(latitude)
			        .longitude(longitude)
			        .visitedAt(visitedAt)
			        .description(description)
			        .address(address)
			        .image(image)
			        .sido(sido)
			        .gugun(gugun)
			        .category(category)
			        .build();
		}

	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class SearchCondition {
		
		private Integer page;
		private Integer size;
		private String sort;
		private String sortDir;
		
		private List<Long> sidoIds;
		private Long categoryId;
		private String keyword;
		
		private Boolean mine;
		
		public MemberPlaceSearchCondition toEntity() {
			return new MemberPlaceSearchCondition(page, size, sort, sortDir, null, sidoIds, categoryId, keyword);
		}

	}

}
