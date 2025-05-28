package com.ssafy.trip.domain.article.qna;

import com.ssafy.trip.domain.article.Article;
import com.ssafy.trip.domain.member.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleAnswer {
	
    private Article article;
    private QnaAnswer answer;
    
}
