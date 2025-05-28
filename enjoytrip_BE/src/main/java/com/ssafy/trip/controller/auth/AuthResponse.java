package com.ssafy.trip.controller.auth;

import com.ssafy.trip.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public class AuthResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Message {
        private final String message;
        private final Boolean result;

        public static Message from(Boolean result, String message) {
            return Message.builder()
                    .message(message)
                    .result(result)
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class SignIn {
        private final String email;
        private final String name;
        private final String profileImage;

        public static SignIn from(Member member) {
            return SignIn.builder()
                    .email(member.getEmail())
                    .name(member.getName())
                    .profileImage(member.getProfileImage())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Login {
        private Data data;
        private String message; // 계정이 연동되었음을 알림.
        private String result;  // 로그인 성공 여부

        public static Login from(Data data, String message, String result) {
            return new Login(
                    data,
                    message,
                    result
            );
        }

        @ToString
        @Getter
        @Builder
        @AllArgsConstructor
        public static class Data {
            private final Long id;
            private final String email;
            private final String name;
            private final String profileImage;
            private final String role;

            public static Data from(Member member) {
                return Data.builder()
                        .id(member.getId())
                        .email(member.getEmail())
                        .name(member.getName())
                        .profileImage(member.getProfileImage())
                        .role(member.getRole().name())
                        .build();
            }
        }
    }
}
