package com.ssafy.trip.domain.attraction.region;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Gugun {
	
	private Long id;
	private Integer sidoCode;
	private Integer gugunCode;
	private String gugunName;
	
}
