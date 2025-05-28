package com.ssafy.trip.controller.admin.article;

import com.ssafy.trip.domain.article.Article;
import com.ssafy.trip.domain.article.ArticleSearchCondition;
import com.ssafy.trip.domain.article.enums.ArticleSortType;
import com.ssafy.trip.domain.article.enums.ArticleType;
import com.ssafy.trip.domain.article.enums.ReportStatus;
import com.ssafy.trip.domain.article.qna.QnaArticleSearchCondition;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AdminArticleRequest {

	@Getter
	@Setter
	@NoArgsConstructor
	public static class Data  {
		private String title;
		private String content;
		
		public Article toEntity() {
			return Article.builder()
				.title(title)
				.content(content)
				.articleType(ArticleType.NOTICE)
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
	@Setter
	@NoArgsConstructor
	public static class QnaAnswerCreate  {
		private Long questionId;
		private Long answerId;
		private String content;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	public static class ReportSearchCondition {

		private Integer page;
		private Integer size;
		private String status;
		
		public com.ssafy.trip.domain.article.interaction.ReportSearchCondition toEntity() {
			ReportStatus reportStatus = null;
			if(status != null && !status.isBlank()) {
				reportStatus = ReportStatus.valueOf(status.toUpperCase());
			}
			return new com.ssafy.trip.domain.article.interaction.ReportSearchCondition(page, size, reportStatus); 
		}
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	public static class ReportRequest {
		private String status;
		
		public ReportStatus toStatus() {
			return ReportStatus.valueOf(status.toUpperCase());
		}
	}
	
}
