package com.ssafy.trip.external.oauth.dto;

public record NaverUser(
        String resultcode,
        String message,
        Response response
) {
    public record Response(
            String id,
            String profile_image,
            String email,
            String name,
            String birthday,
            String birthyear
    ) {}
}
