package com.ssafy.trip.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
	@Bean
    public RestTemplate restTemplate() {
        // 타임아웃 설정
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);  // 연결 타임아웃 (ms)
        factory.setReadTimeout(5000);     // 응답 타임아웃 (ms)

        return new RestTemplate(factory);
    }
}
