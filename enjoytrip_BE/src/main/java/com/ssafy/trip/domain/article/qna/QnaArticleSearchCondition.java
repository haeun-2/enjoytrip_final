package com.ssafy.trip.domain.article.qna;

import com.ssafy.trip.domain.article.enums.ArticleSortType;
import com.ssafy.trip.domain.article.enums.ArticleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class QnaArticleSearchCondition {
	
	private Integer page;
	private Integer size;
	private String title;
	private String status;
	private Long memberId;

	public QnaArticleSearchCondition() {
		this(null, null, null, null, null);
	}
	
	public QnaArticleSearchCondition(Integer page, Integer size, String title, String status) {
		this.page = (page == null || page < 1) ? 1 : page;
        this.size = (size == null || size < 1) ? 10 : size;
        this.title = title;
        this.status = status;
	}
	
	public int getOffset() {
        return (page - 1) * size;
    }
    
    public void setMemberId(Long memberId) {
    	this.memberId = memberId;
    }
	
}
