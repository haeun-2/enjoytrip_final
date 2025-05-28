package com.ssafy.trip.domain.article.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ArticleSortType {

	CREATED_AT, VIEW_COUNT, LIKE_COUNT, COMMENT_COUNT;
	
	@JsonCreator
	 public static ArticleSortType from(String s) {
		 if(s == null) return CREATED_AT;
		return ArticleSortType.valueOf(s.toUpperCase());
	 }
	
}
