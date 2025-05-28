package com.ssafy.trip.controller.admin.member;

import com.ssafy.trip.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AdminMemberResponse {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class Info {
        private Long id;
        private String name;
        private String profileImage;
        private String email;
        private LocalDateTime createdAt;
        private LocalDateTime deletedAt;
        private LocalDateTime blockedAt;
        private String status;
        private LocalDate birthday;

        public static Info from(Member member) {
            return Info.builder()
                    .id(member.getId())
                    .name(member.getName())
                    .profileImage(member.getProfileImage())
                    .email(member.getEmail())
                    .createdAt(member.getCreatedAt())
                    .deletedAt(member.getDeletedAt())
                    .blockedAt(member.getBlockedAt())
                    .birthday(member.getBirthDate())
                    .status(member.getBlockedAt() != null ? "정지" : member.getDeletedAt() != null ? "탈퇴" : "활성")
                    .build();
        }
    }
}
