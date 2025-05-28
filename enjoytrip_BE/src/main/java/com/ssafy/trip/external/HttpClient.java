package com.ssafy.trip.external;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HttpClient {
	
	private final RestTemplate restTemplate;
	
	
	public <T> T get(String url, HttpHeaders headers, Class<T> responseType) {
		HttpEntity<Void> request = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, request, responseType).getBody();
    }
	
	
	public <T> T post(String url, Object requestBody, HttpHeaders headers, Class<T> responseType) {
		HttpEntity<Object> request = new HttpEntity<>(requestBody, headers);
        return restTemplate.exchange(url, HttpMethod.POST, request, responseType).getBody();
    }
	
	
	public String getValueFromResponse(String response, String key) {
		 ObjectMapper objectMapper = new ObjectMapper();
			try {
				JsonNode rootNode = objectMapper.readTree(response);
				return rootNode.get(key).asText();
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		    return "";
	}
}
