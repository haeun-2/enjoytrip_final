package com.ssafy.trip.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(info = @Info(title = "ENJOY TRIP API 명세서", description = "API 명세서입니다.", version = "v1"))
public class SwaggerConfig {
	
	@Bean
    GroupedOpenApi memberLikeOpenApi() {
        String[] paths = { "/api/members/*/like/**" };
        return GroupedOpenApi.builder().group("Member Like 관련 API").pathsToMatch(paths).build();
    }
	
    @Bean
    GroupedOpenApi memberOpenApi() {
        String[] paths = { "/api/members/**" };
        String[] excludePaths = {"/api/members/*/like/**"};
        return GroupedOpenApi.builder().group("Member 관련 API").pathsToMatch(paths).build();
    }

    @Bean
    GroupedOpenApi otherOpenApi() {
        String[] paths = { "/api/auth/**" };
        return GroupedOpenApi.builder().group("Auth 관련 API").pathsToMatch(paths).build();
    }

    @Bean
    GroupedOpenApi regionOpenApi() {
        String[] paths = { "/api/region/**" };
        return GroupedOpenApi.builder().group("Region 관련 API").pathsToMatch(paths).build();
    }
    
    @Bean
    GroupedOpenApi attractionOpenApi() {
        String[] paths = { "/api/attractions/**" };
        return GroupedOpenApi.builder().group("Attraction 관련 API").pathsToMatch(paths).build();
    }
    
    @Bean
    GroupedOpenApi memberPlaceOpenApi() {
        String[] paths = { "/api/hotplaces/**" };
        return GroupedOpenApi.builder().group("Hotplace 관련 API").pathsToMatch(paths).build();
    }

    @Bean
    GroupedOpenApi commentOpenApi() {
        String[] paths = { "/api/articles/*/comments/**" };
        return GroupedOpenApi.builder().group("Comment 관련 API").pathsToMatch(paths).build();
    }

    @Bean
    GroupedOpenApi articleOpenApi() {
        String[] paths = {"/api/articles/**"};
        String[] excludePaths = {"/api/articles/*/comments/**"};
        return GroupedOpenApi.builder().group("Article 관련 API")
                .pathsToMatch(paths)
                .pathsToExclude(excludePaths)
                .build();
    }

    @Bean
    GroupedOpenApi planOpenApi() {
        String[] paths = { "/api/plans/**" };
        return GroupedOpenApi.builder().group("Trip Plan 관련 API").pathsToMatch(paths).build();
    }
    
    @Bean
    GroupedOpenApi uploadOpenApi() {
        String[] paths = { "/api/upload/**" };
        return GroupedOpenApi.builder().group("이미지 Upload 관련 API").pathsToMatch(paths).build();
    }

    @Bean
    GroupedOpenApi adminOpenApi() {
        String[] paths = { "/api/admin/**" };
        return GroupedOpenApi.builder().group("Admin 관련 API").pathsToMatch(paths).build();
    }
}
