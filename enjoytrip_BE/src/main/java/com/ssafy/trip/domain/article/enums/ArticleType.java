package com.ssafy.trip.domain.article.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ArticleType {
	NOTICE, FREE, QNA;
	
	 @JsonCreator
	 public static ArticleType from(String s) {
		 if(s == null) return FREE;
		return ArticleType.valueOf(s.toUpperCase());
	 }
}
