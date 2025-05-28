package com.ssafy.trip.controller.attraction;

import java.util.List;

import com.ssafy.trip.common.wordsearch.SearchEngine;
import com.ssafy.trip.domain.attraction.Attraction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class AttractionResponse {

	@Builder
	@Getter
	@AllArgsConstructor
	public static class Info {
		private Long id;
		private String name;
		private String address;
		private String image;
		private int likeCount;
		private String category;
		private Double latitude;
		private Double longitude;
		private boolean isLiked;

		public static Info from(Attraction attraction) {
			return Info.builder()
					.id(attraction.getId())
					.name(attraction.getTitle())
					.address(attraction.getAddress1())
					.image(attraction.getImage1())
					.latitude(attraction.getLatitude())
					.longitude(attraction.getLongitude())
					.likeCount(attraction.getLikeCount())
					.isLiked(attraction.isLiked())
					.category(attraction.getCategory().getCategoryName())
					.build();
		}
	}

	@Getter
	@AllArgsConstructor
	public static class SearchInfo {
		private AttractionRequest.SearchCondition condition;
		private List<Attraction> attractions;
	}

	@Getter
	@AllArgsConstructor
	public static class Name {
		private String word;
		private Long id;

		public static Name from(SearchEngine.Node node) {
			return new Name(node.getWord(), node.getId());
		}
	}
}
