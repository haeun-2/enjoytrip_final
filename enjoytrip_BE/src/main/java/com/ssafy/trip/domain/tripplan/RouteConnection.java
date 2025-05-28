package com.ssafy.trip.domain.tripplan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteConnection {
    private Long id;
    private Long fromItemId;
    private Long toItemId;
    private String distance;
    private String duration;

    
}
