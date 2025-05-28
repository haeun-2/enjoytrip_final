package com.ssafy.trip.external.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.ssafy.trip.external.HttpClient;
import com.ssafy.trip.external.oauth.dto.NaverUser;

@Component
@RequiredArgsConstructor
public class NaverOAuthClient {
    private final HttpClient httpClient;

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSercret;

    @Value("${naver.oauth.redirect.uri}")
    private String redirectUri;

    public String getAccessToken(String code, String state) {
        String url = "https://nid.naver.com/oauth2.0/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("client_secret", clientSercret);
        body.add("state", state);
        body.add("code", code);

        String response = httpClient.post(url, body, headers, String.class);
        return httpClient.getValueFromResponse(response, "access_token");
    }

    public NaverUser getUserInfo(String accessToken) {
        String url = "https://openapi.naver.com/v1/nid/me";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+accessToken);

        NaverUser response = httpClient.get(url, headers, NaverUser.class);
        return response;
    }
}
