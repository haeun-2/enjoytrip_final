package com.ssafy.trip.domain.attraction;

import com.ssafy.trip.service.attraction.AttractionService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttractionSearchCondition {
	private Integer sidoCode;
	private Integer gugunCode;
	private Integer categoryId;

	private Double minLat;
	private Double maxLat;
	private Double minLon;
	private Double maxLon;

	private Long attractionId;
	private Long loginMemberId;

	private String sort;
	private int offset;
	private int size;


	public static  AttractionSearchCondition from(AttractionService.BoxArea boxArea, Integer categoryId) {
		return AttractionSearchCondition.builder()
				.minLat(boxArea.minLat())
				.maxLat(boxArea.maxLat())
				.minLon(boxArea.minLon())
				.maxLon(boxArea.maxLon())
				.categoryId(categoryId)
				.build();
	}

	public AttractionSearchCondition(Integer sidoCode, Integer gugunCode, Integer categoryId) {
		this.sidoCode = sidoCode;
		this.gugunCode = gugunCode;
		this.categoryId = categoryId;
	}
}
