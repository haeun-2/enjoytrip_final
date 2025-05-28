package com.ssafy.trip.domain.memberplace;

import java.time.LocalDate;

import com.ssafy.trip.domain.attraction.Category;
import com.ssafy.trip.domain.attraction.region.Gugun;
import com.ssafy.trip.domain.attraction.region.Sido;
import com.ssafy.trip.domain.member.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberPlace {

	private Long id;
	private LocalDate createdAt;
	private LocalDate modifiedAt;
	private String name;
	private Double latitude;
	private Double longitude;
	private LocalDate visitedAt;
	private String description;
	private String address;
	private String image;
	private Integer likeCount;
	private LocalDate deletedAt;
	
	private Member member;
	private Sido sido;
	private Gugun gugun;
	private Category category;

	private Long memberId;
	private String memberName;
	private Long sidoId;
	private String sidoName;
	private Long gugunId;
	private String gugunName;
	private Long categoryId;
	private String categoryName;
	
	private Boolean isLiked;

	public void update(String name, Double latitude, Double longitude, LocalDate visitedAt, String description, String address, String image, Sido sido, Gugun gugun, Category category) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.visitedAt = visitedAt;
		this.description = description;
		this.address = address;
		this.image = image;
		this.sido = sido;
		this.gugun = gugun;
		this.category = category;
	}
	
	public void setMember(Member member) {
		this.member = member;
	}

	public void addLikeCount() {
		likeCount++;
	}

	public void subLikeCount() {
		likeCount--;
	}
}
