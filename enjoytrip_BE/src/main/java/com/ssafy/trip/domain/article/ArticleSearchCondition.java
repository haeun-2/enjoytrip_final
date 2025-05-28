package com.ssafy.trip.domain.article;

import com.ssafy.trip.domain.article.enums.ArticleSortType;
import com.ssafy.trip.domain.article.enums.ArticleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ArticleSearchCondition {
	
	// 페이징
	private Integer page;
	private Integer size;
	
	// 정렬
	private ArticleSortType sort;
	
	// 핕터링
	private String title;
	private Long memberId;
	
	// 게시판 유형
	private ArticleType articleType;
	
	private Boolean deleted;

	public ArticleSearchCondition() {
		this(null, null, null, null, null);
	}
	
	public ArticleSearchCondition(Integer page, Integer size, ArticleSortType sort, String title, ArticleType articleType) {
		this.page = (page == null || page < 1) ? 1 : page;
        this.size = (size == null || size < 1) ? 10 : size;
        this.sort = (sort == null) ? ArticleSortType.CREATED_AT : sort;
        this.title = title;
        this.articleType = (articleType == null) ? ArticleType.FREE : articleType;
        this.deleted = false;
	}
	
	public int getOffset() {
        return (page - 1) * size;
    }
    
    public void setMemberId(Long memberId) {
    	this.memberId = memberId;
    }
    
    public void showDeleted() {
    	this.deleted = true;
    }
	
}
