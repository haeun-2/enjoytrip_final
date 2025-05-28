package com.ssafy.trip.controller.member;

import com.ssafy.trip.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MemberResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Info {
        private String email;
        private String name;
        private String profileImage;
        private LocalDate birthDate;
        private LocalDateTime createdAt;

        public static Info from(Member member) {
            return Info.builder()
                    .email(member.getEmail())
                    .name(member.getName())
                    .profileImage(member.getProfileImage())
                    .birthDate(member.getBirthDate())
                    .createdAt(member.getCreatedAt())
                    .build();
        }
    }
}
