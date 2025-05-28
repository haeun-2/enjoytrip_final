package com.ssafy.trip.controller.memberplace;

import java.time.LocalDate;

import com.ssafy.trip.domain.attraction.Category;
import com.ssafy.trip.domain.attraction.region.Gugun;
import com.ssafy.trip.domain.attraction.region.Sido;
import com.ssafy.trip.domain.memberplace.MemberPlace;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class MemberPlaceResponse {
	
	@ToString
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Info {
		
		private Long id;
		private String name;
		private Double latitude;
		private Double longitude;
		private LocalDate visitedAt;
		private String description;
		private String address;
		private String image;
		
		private Sido sido;
		private Gugun gugun;
		private Category category;
		private Member member;
	
		private Integer likeCount;
		
		private Boolean isLiked;
		
		public static Info from(MemberPlace mp) {
			return Info.builder()
					.id(mp.getId())
			        .name(mp.getName())
			        .latitude(mp.getLatitude())
			        .longitude(mp.getLongitude())
			        .visitedAt(mp.getVisitedAt())
			        .description(mp.getDescription())
			        .address(mp.getAddress())
			        .image(mp.getImage())
			        .likeCount(mp.getLikeCount())
			        .sido(mp.getSido())
			        .gugun(mp.getGugun())
			        .category(mp.getCategory())
			        .member(Member.from(mp.getMember()))
			        .isLiked(mp.getIsLiked())
			        .build();
		}

	}
	
	@Builder
	@AllArgsConstructor
	@Getter
	private static class Member {
		private Long id;
		private String name;
		
		public static Member from(com.ssafy.trip.domain.member.Member member) {
			return Member.builder()
					.id(member.getId())
					.name(member.getName())
					.build();
		}
	}

}
