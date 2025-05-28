package com.ssafy.trip.controller.tripplan;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssafy.trip.common.controller.PageRequest;
import com.ssafy.trip.common.controller.Sort;
import com.ssafy.trip.domain.tripplan.PlanItem;
import com.ssafy.trip.domain.tripplan.RouteConnection;
import com.ssafy.trip.domain.tripplan.TripPlan;
import com.ssafy.trip.domain.tripplan.TripPlanDay;
import com.ssafy.trip.domain.tripplan.TripPlanSearchCondition;
import com.ssafy.trip.domain.tripplan.enums.Season;
import com.ssafy.trip.domain.tripplan.enums.TripStyle;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class PlanRequest {

    @Getter
    @AllArgsConstructor
    public static class SearchCondition {
        private Sort sort;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String mine;

		private TripStyle tripStyle;
		private List<Season> seasons;
		private List<String> ranges;

        public TripPlanSearchCondition toCondition(Long memberId, Long loginMemberId, PageRequest pageRequest) {
			List<TripPlanSearchCondition.Range> parsedRanges = (ranges == null) ? null
					:ranges.stream()
					.map(r -> {
						if (r.equals("15~")) return new TripPlanSearchCondition.Range(15, Integer.MAX_VALUE);
						String[] parts = r.split("-");
						return new TripPlanSearchCondition.Range(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
					})
					.toList();
			return TripPlanSearchCondition.builder()
					.offset(pageRequest.getOffset())
					.size(pageRequest.getSize())
					.sort(sort.getValue())
					.memberId(memberId)
					.loginMemberId(loginMemberId)
					.tripStyle(tripStyle)
					.seasons(seasons)
					.ranges(parsedRanges)
					.build();
		}
    }

    @Getter
    @AllArgsConstructor
    public static class Create {
        private String title;
        private LocalDate startDate;
        private LocalDate endDate;
        
        public TripPlan toEntity(int totalDays, Season season) {
        	return TripPlan.builder()
        			.title(title)
        			.startDate(startDate)
        			.endDate(endDate)
					.totalDays(totalDays)
					.season(season)
        			.build();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class CreateDetail {
        private String title; // 수정 가능, 덮어쓰기
        private String description;
        private List<TripPlanDayInfo> tripPlanDayInfo;
        
        public TripPlan toEntity(Long id) {
        	return TripPlan.builder()
        			.id(id)
        			.title(title)
        			.description(description)
        			.build();
        }
        
        @Getter
        @AllArgsConstructor
        public static class TripPlanDayInfo {
        	private LocalDate date;
        	private int dayOrder;
        	private List<ItemInfo> items;
        	
        	public TripPlanDay toEntity(Long tripPlanId) {
            	return TripPlanDay.builder()
            			.date(date)
            			.dayOrder(dayOrder)
            			.tripPlanId(tripPlanId)
            			.build();
        	}
        	
        	@Getter
        	@AllArgsConstructor
        	public static class ItemInfo {
        		private String description;
        		private int itemOrder;
        		private String itemType;
        		private Long attractionId;
        		private Long memberPlaceId;
        		private Route routeFromPrevious;
        		
        		public PlanItem toEntity(Long tripPlanDayId, Long attractionId, Long memberPlaceId) {
        			return PlanItem.builder()
        					.description(description)
        					.itemOrder(itemOrder)
        					.itemType(itemType)
        					.tripPlanDayId(tripPlanDayId)
        					.attractionId(attractionId)
        					.memberPlaceId(memberPlaceId)
        					.build();
        		}
        		
        		@Getter
        		@AllArgsConstructor
        		public static class Route {
        			private String distance;
        			private String duration;
        			
        			
        			public RouteConnection toEntity(Long fromItemId, Long toItemId) {
        				return RouteConnection.builder()
        						.fromItemId(fromItemId)
        						.toItemId(toItemId)
        						.distance(distance)
        						.duration(duration)
        						.build();
        			}
        		}
        	}
        }
    }
}