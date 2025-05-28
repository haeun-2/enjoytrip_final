package com.ssafy.trip.domain.attraction;

import com.ssafy.trip.domain.attraction.region.Gugun;
import com.ssafy.trip.domain.attraction.region.Sido;
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
public class Attraction {
	
	private Long id;
	private Integer contentId;
	private String title;
	private String image1;
	private String image2;
	private Integer mapLevel;
	private Double latitude;
	private Double longitude;
	private String tel;
	private String address1;
	private String address2;
	private String homepage;
	private String overview;
	private int likeCount;
	
	private Sido sido;
	private Gugun gugun;
	private Category category;
	// 좋아요 여부 판단
	private boolean isLiked;

	public void addLikeCount() {
		likeCount++;
	}
	public void subLikeCount() {
		likeCount--;
	}
}
