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
public class Sido {
	
	private Long id;
	private Integer sidoCode;
	private String sidoName;
	
}
