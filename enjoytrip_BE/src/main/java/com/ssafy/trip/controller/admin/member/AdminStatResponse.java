package com.ssafy.trip.controller.admin.member;


import lombok.*;

import java.util.List;

public class AdminStatResponse {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Count {
        private Long totalMember;
        private Long totalArticle;
        private Long totalOpenReport;
        private Long totalOpenQnA;

        private Long prevMember;
        private Long prevArticle;
        private Long prevOpenReport;
        private Long prevOpenQnA;

        private Long nowMember;
        private Long nowArticle;
        private Long nowOpenReport;
        private Long nowOpenQnA;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberCount {
        private Long totalMember;
        private Long activeMember;
        private Long deletedMember;
        private Long blockedMember;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MonthlyStat {
        List<Integer> monthLabel;
        List<Integer> articles;
        List<Integer> members;
        List<Integer> openReports;
        List<Integer> openQnAs;
        List<Integer> plans;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MonthlyRow {
        private Integer month;
        private Integer articleCount;
        private Integer memberCount;
        private Integer reportCount;
        private Integer qnaCount;
        private Integer planCount;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoryCount {
        private Integer relaxing;
        private Integer sightSeeing;
        private Integer nature;
        private Integer cultural;
        private Integer foodie;
        private Integer adventure;
        private Integer shopping;
        private Integer travel;
    }

}
