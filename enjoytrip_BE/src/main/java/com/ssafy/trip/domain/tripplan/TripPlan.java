package com.ssafy.trip.domain.tripplan;

import com.ssafy.trip.domain.member.Member;

import com.ssafy.trip.domain.tripplan.enums.Season;
import com.ssafy.trip.domain.tripplan.enums.TripStyle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripPlan {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long memberId;
    private int likeCount;
    private int viewCount;
    private int totalDays;
    private Season season;

    private List<TripStyle> tripStyles;
    private List<TripPlanDay> tripPlanDays;
    private Member member;
    // TODO: 좋아요 여부 판단 -> DTO로 이동 필요
    private int isLiked;
    
    public void setAuthor(Long memberId) {
    	this.memberId = memberId;
    }

    public void addLikeCount() {
        this.likeCount++;
    }
    public void updateViewCount() {
        this.viewCount+=1;
    }
    public void subLikeCount() {
        this.likeCount--;
    }
}
