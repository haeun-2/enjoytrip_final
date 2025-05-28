package com.ssafy.trip.domain.tripplan;

import com.ssafy.trip.domain.tripplan.enums.Season;
import com.ssafy.trip.domain.tripplan.enums.TripStyle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class TripPlanSearchCondition {
    private int offset;
    private int size;
    private String sort;
    private Long memberId; // Optional: null일 경우 전체 검색
    private Long loginMemberId; // Optional: 로그인한 사용자 ID, null이면 로그인하지 않음
    private TripStyle tripStyle;
    private List<Season> seasons;
    private List<Range> ranges;

    // 여행 상태 별
    private String status;
    private LocalDate now;

    @AllArgsConstructor
    public static class Range {
        private int start;
        private int end;
    }

}