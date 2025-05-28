package com.ssafy.trip.external.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.ssafy.trip.external.HttpClient;
import com.ssafy.trip.external.oauth.dto.KakaoUser;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class KakaoOAuthClient {
	private final HttpClient httpClient;
	
	@Value("${kakao.client.id}")
	private String clientId;
	
	@Value("${kakao.oauth.redirect.uri}")
	private String redirectUri;
	
	
	public String getAccessToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        body.add("client_id", clientId);

        String response = httpClient.post(url, body, headers, String.class);
        return httpClient.getValueFromResponse(response, "access_token");
    }
	
	
	public KakaoUser getUserInfo(String accessToken) {
		String url = "https://kapi.kakao.com/v1/oidc/userinfo";
		HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+accessToken);
        
        KakaoUser response = httpClient.get(url, headers, KakaoUser.class);
        return response;
	}
}
