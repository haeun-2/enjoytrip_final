package com.ssafy.trip.external.oauth.dto;


public record KakaoUser(
		String sub,
		String nickname,
		String picture,
		String email
) {}
