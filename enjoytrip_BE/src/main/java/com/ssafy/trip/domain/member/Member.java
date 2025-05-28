package com.ssafy.trip.domain.member;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ssafy.trip.domain.auth.enums.Role;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class Member {
	private Long id;
	private String email;
	private String password;
	private String name;
	private Role role;
	private LocalDateTime createdAt;
	private LocalDateTime deletedAt;
	private LocalDateTime blockedAt;
	private LocalDate birthDate;
	private String profileImage;
	
	@Builder
	public Member (String email, String password, String name, Role role, LocalDateTime createdAt, LocalDate birthDate, String profileImage) {
		this.email = email;
		this.password = password;
		this.role = role;
		this.createdAt = createdAt;
		this.name = name;
		this.birthDate = birthDate;
		this.profileImage = profileImage;
	}

	public void encodePassword(String encodedPassword) {
		this.password = encodedPassword;
	}

	public void update(String name, String profileImage, LocalDate birthDate) {
		this.name = name;
		this.profileImage = profileImage;
		this.birthDate = birthDate;
	}
	
	public void delete() {
		this.deletedAt = LocalDateTime.now();
	}

	public boolean isDeleted() {
		return this.deletedAt != null;
	}
}
