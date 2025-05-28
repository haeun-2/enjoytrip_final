package com.ssafy.trip.domain.article;

import java.time.LocalDateTime;

import com.ssafy.trip.domain.article.enums.ArticleType;
import com.ssafy.trip.domain.member.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Getter
public class Article {

	private String title;
	private String content;
	private Integer viewCount;
	private Integer likeCount;
	private Integer commentCount;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	private LocalDateTime deletedAt;
	private Member member;
	private ArticleType articleType;

	private boolean isLiked;private Long id;

	
	@Builder
	public Article(String title, String content, Member member, ArticleType articleType) {
		this.title = title;
		this.content = content;
		this.member = member;
		this.articleType = articleType;
	}
	
	public void updateViewCount() {
		this.viewCount += 1;
	}
	public void addLikeCount() {
		this.likeCount += 1;
	}

	public void subLikeCount() {
		this.likeCount -= 1;
	}
	
	public void update(String title, String content) {
		this.title = title;
		this.content = content;
	}
	
	public void setMember(Member member) {
		this.member = member;
	}
}
