package com.ssafy.trip.controller.article;

import com.ssafy.trip.domain.article.Article;
import com.ssafy.trip.domain.article.ArticleSearchCondition;
import com.ssafy.trip.domain.article.enums.ArticleSortType;
import com.ssafy.trip.domain.article.enums.ArticleType;
import com.ssafy.trip.domain.article.qna.QnaArticleSearchCondition;

import lombok.*;

public class ArticleRequest {
	
	@ToString
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Create {
		private String title;
		private String content;
		private String articleType;
		
		public Article toEntity() {
			return Article.builder()
				.title(title)
				.content(content)
				.articleType(ArticleType.from(articleType))
				.build();
		}
	}
	
	@ToString
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Update {
		private String title;
		private String content;
		
		public Article toEntity() {
			return Article.builder()
				.title(title)
				.content(content)
				.build();
		}
	}
	
	
	@Getter
	@Setter
	@NoArgsConstructor
	public static class SearchCondition {

		private Integer page;
		private Integer size;
		private String sort;
		private String title;
		private String articleType;
		private Boolean mine;
		
		public ArticleSearchCondition toEntity() {
			return new ArticleSearchCondition(page, size, ArticleSortType.from(sort), title, ArticleType.from(articleType));
		}
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	public static class QnaSearchCondition {

		private Integer page;
		private Integer size;
		private String title;
		private String status;
		
		public QnaArticleSearchCondition toEntity() {
			return new QnaArticleSearchCondition(page, size, title, status);
		}
	}

	@Getter
	@AllArgsConstructor
	public static class Report{
		private String reason;
	}
	
}
