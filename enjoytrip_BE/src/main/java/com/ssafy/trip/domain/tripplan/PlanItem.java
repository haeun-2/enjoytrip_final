package com.ssafy.trip.domain.tripplan;

import com.ssafy.trip.domain.attraction.Attraction;
import com.ssafy.trip.domain.memberplace.MemberPlace;

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
public class PlanItem {
    private Long id;
    private String description;
    private int itemOrder;
    private String itemType;
    private Long tripPlanDayId; // 부모 plan
    private Long attractionId; // nullable
    private Long memberPlaceId; // nullable

    private Attraction attraction; // nullable
    private MemberPlace memberPlace; // nullable
    private RouteConnection routeFromPrevious;
    
    public void setTripPlanDay(Long tripPlanDayId) {
    	this.tripPlanDayId = tripPlanDayId;
    }
}
