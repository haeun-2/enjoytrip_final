package com.ssafy.trip.domain.article.qna;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QnaAnswer {
	
	private Long id;
    private String content;
    private Long memberId;
    private Long articleId;
    private LocalDateTime createdAt;
    private String status;
 
    public QnaAnswer(Long articleId) {
    	this.articleId = articleId;
    }
    
    public void update(String content, Long memberId) {
    	this.content = content;
    }
    
}
