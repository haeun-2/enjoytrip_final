package com.ssafy.trip.controller.tripplan;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssafy.trip.domain.attraction.Attraction;
import com.ssafy.trip.domain.attraction.Category;
import com.ssafy.trip.domain.memberplace.MemberPlace;
import com.ssafy.trip.domain.tripplan.TripPlan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlanResponse {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class Summary {
        private Long id;
        private String title;
        private String description;
        private LocalDate startDate;
        private LocalDate endDate;
        private int likeCount;
        private int viewCount;
        private int totalDays;
        private LocalDateTime createdAt;
        // 작성자 정보
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String memberName;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String memberProfileUrl;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String categories;
        // 좋아요 여부
        private boolean isLiked;

        public static Summary from(TripPlan plan) {
            return Summary.builder()
                    .id(plan.getId())
                    .title(plan.getTitle())
                    .description(plan.getDescription())
                    .startDate(plan.getStartDate())
                    .endDate(plan.getEndDate())
                    .createdAt(plan.getCreatedAt())
                    .likeCount(plan.getLikeCount())
                    .viewCount(plan.getViewCount())
                    .totalDays(plan.getTotalDays())
                    .isLiked(plan.getIsLiked()==1)
                    .categories(plan.getTripStyles() != null ? plan.getTripStyles().stream().map(Objects::toString).collect(Collectors.joining(",")) : null)
                    .memberName(plan.getMember() != null ? plan.getMember().getName() : null)
                    .memberProfileUrl(plan.getMember() != null ? plan.getMember().getProfileImage() : null)
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Info {
        private Long id;
        private String title;
        private String description;
        private LocalDate startDate;
        private LocalDate endDate;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        // 작성자 정보
        private String memberName;
        private String memberProfileUrl;
        // 각 여행지 정보
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private List<DailySchedule> dailySchedules;
        // 통계
        private int likeCount;
        private int viewCount;
        private String categories;

        @Getter
        @Builder
        @AllArgsConstructor
        public static class DailySchedule {
            private Long id;
            private LocalDate date;
            private int dayOrder;
            @JsonInclude(JsonInclude.Include.NON_EMPTY)
            private List<Item> items;

            @Getter
            @Builder
            @AllArgsConstructor
            public static class Item {
                private Long id;
                private String description;
                private int itemOrder;
                private String itemType;
                // 여행지 정보 조회를 위해 id를 포함
                private Long attractionId; // nullable
                private Long memberPlaceId; // nullable
                // 여행지 정보
                private String placeName;
                private String placeImageUrl;
                private String placeAddress;
                private String placeCategory;
                private Double latitude;
                private Double longitude;
                // 이동 경로 정보
                private Route routeFromPrevious;

                @Getter
                @Builder
                @AllArgsConstructor
                public static class Route {
                    private String distance;
                    private String duration;
                }
            }
        }
        
        public static Info from(TripPlan tripPlan) {
            // TODO: 리팩토링 필요
            return Info.builder()
                    .id(tripPlan.getId())
                    .title(tripPlan.getTitle())
                    .description(tripPlan.getDescription())
                    .startDate(tripPlan.getStartDate())
                    .endDate(tripPlan.getEndDate())
                    .createdAt(tripPlan.getCreatedAt())
                    .modifiedAt(tripPlan.getModifiedAt())
                    .memberName(tripPlan.getMember().getName())
                    .memberProfileUrl(tripPlan.getMember().getProfileImage())
                    .likeCount(tripPlan.getLikeCount())
                    .viewCount(tripPlan.getViewCount())
                    .categories(tripPlan.getTripStyles() != null ? tripPlan.getTripStyles().stream().map(Objects::toString).collect(Collectors.joining(",")) : null)
                    .dailySchedules(tripPlan.getTripPlanDays().stream()
                            .map(day -> DailySchedule.builder()
                                .id(day.getId())
                                .date(day.getDate())
                                .dayOrder(day.getDayOrder())
                                .items(
                                    day.getPlanItems().stream()
                                        .map(item -> DailySchedule.Item.builder()
                                            .id(item.getId())
                                            .description(item.getDescription())
                                            .itemOrder(item.getItemOrder())
                                            .itemType(item.getItemType())
                                            .attractionId(item.getAttractionId())
                                            .memberPlaceId(item.getMemberPlaceId())
                                            .placeName(item.getAttraction() != null ? item.getAttraction().getTitle() : item.getMemberPlace().getName())
                                            .placeAddress(item.getAttraction() != null ? item.getAttraction().getAddress1() : item.getMemberPlace().getAddress())
                                            .placeImageUrl(item.getAttraction() != null ? item.getAttraction().getImage1() : item.getMemberPlace().getImage())
                                            .placeCategory(
                                                    Optional.ofNullable(item.getAttraction())
                                                            .map(Attraction::getCategory)
                                                            .map(Category::getCategoryName)
                                                            .orElseGet(() -> Optional.ofNullable(item.getMemberPlace())
                                                                    .map(MemberPlace::getCategory)
                                                                    .map(Category::getCategoryName)
                                                                    .orElse(null))
                                            )
                                            .latitude(item.getAttraction() != null ? item.getAttraction().getLatitude() : item.getMemberPlace().getLatitude())
                                            .longitude(item.getAttraction() != null ? item.getAttraction().getLongitude() : item.getMemberPlace().getLongitude())
                                            .routeFromPrevious(item.getRouteFromPrevious() == null ? null :
                                                DailySchedule.Item.Route.builder()
                                                    .distance(item.getRouteFromPrevious().getDistance())
                                                    .duration(item.getRouteFromPrevious().getDuration())
                                                    .build()
                                            )
                                            .build()
                                        ).toList()
                                )
                                .build()
                            ).toList()
                    )
                    .build();
        }
    }
}
