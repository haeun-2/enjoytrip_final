package com.ssafy.trip.controller.auth;

import com.ssafy.trip.domain.auth.enums.Role;
import com.ssafy.trip.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;


public class AuthRequest {

    @Getter
    @AllArgsConstructor
    public static class Login {
        private String email;
        private String password;
    }

    @Getter
    @AllArgsConstructor
    public static class SignIn {
        private String email;
        private String password;
        private String name;
        private LocalDate birthDate;
        private String profileImage;

        public Member toEntity() {
            return Member.builder()
                    .email(email)
                    .password(password)
                    .name(name)
                    .role(Role.USER)
                    .birthDate(birthDate)
                    .profileImage(profileImage)
                    .build();
        }
    }
}
