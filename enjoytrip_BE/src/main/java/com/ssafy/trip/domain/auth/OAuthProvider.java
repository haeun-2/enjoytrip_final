package com.ssafy.trip.domain.auth;

import com.ssafy.trip.domain.auth.enums.Role;
import com.ssafy.trip.domain.member.Member;
import com.ssafy.trip.external.oauth.dto.KakaoUser;
import com.ssafy.trip.external.oauth.dto.NaverUser;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OAuthProvider {
	private Long id;
	private String provider;
	private String key;
	private Long memberId;

	private Member member;

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public static OAuthProvider from(NaverUser user) {
		return OAuthProvider.builder()
				.provider("NAVER")
				.key(user.response().id())
				.member(Member.builder()
						.email(user.response().email())
						.name(user.response().name())
						.profileImage(user.response().profile_image())
						.role(Role.USER)
						.createdAt(LocalDateTime.now())
						.build())
				.build();
	}

	public static OAuthProvider from(KakaoUser user) {
		return OAuthProvider.builder()
				.provider("KAKAO")
				.key(user.sub())
				.member(Member.builder()
						.email(user.email())
						.name(user.nickname())
						.profileImage(user.picture())
						.role(Role.USER)
						.createdAt(LocalDateTime.now())
						.build())
				.build();
	}
}
