package com.ssafy.trip.controller.member;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class MemberRequest {
	
	@Getter
	@AllArgsConstructor
	public static class Update {
		private String name;
		private LocalDate birthDate;
		private String profileImage;
	}

	@Getter
	@AllArgsConstructor
	public static class UpdatePassword {
		private String currentPassword;
		private String newPassword;
	}
}
