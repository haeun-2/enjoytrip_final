package com.ssafy.trip.controller.article;

import java.time.LocalDateTime;

import com.ssafy.trip.domain.article.Article;
import com.ssafy.trip.domain.article.enums.ArticleType;
import com.ssafy.trip.domain.article.qna.ArticleAnswer;
import com.ssafy.trip.domain.article.qna.QnaAnswer;
import com.ssafy.trip.domain.auth.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


public class ArticleResponse {

	@Builder
	@AllArgsConstructor
	@Getter
	public static class Info {
		private Long id;
		private String title;
		private String content;
		private Integer viewCount;
		private Integer likeCount;
		private Integer commentCount;
		private LocalDateTime createdAt;
		private LocalDateTime modifiedAt;
		private LocalDateTime deletedAt;

		private boolean isLiked;
		
		private Member member;
		
		private ArticleType articleType;
		
		public static Info from(Article article) {
			return Info.builder()
					.id(article.getId())
					.title(article.getTitle())
					.content(article.getContent())
					.viewCount(article.getViewCount())
					.likeCount(article.getLikeCount())
					.commentCount(article.getCommentCount())
					.createdAt(article.getCreatedAt())
					.modifiedAt(article.getModifiedAt())
					.deletedAt(article.getDeletedAt())
					.member(Member.from(article.getMember()))
					.articleType(article.getArticleType())
					.isLiked(article.isLiked())
					.build();
		}
		
	}
	
	@Builder
	@AllArgsConstructor
	@Getter
	public static class QnaInfo {
		private Info question;
		private QnaAnswer answer;
		private String status;
		
		public static QnaInfo from(ArticleAnswer articleAnswer) {
			return QnaInfo.builder()
						.question(Info.from(articleAnswer.getArticle()))
						.answer(articleAnswer.getAnswer())
						.status(articleAnswer.getAnswer().getStatus())
						.build();
		}
		
	}
	
	@Builder
	@AllArgsConstructor
	@Getter
	private static class Member {
		private Long id;
		private String name;
		private Role role;
		private String profileImage;
		
		public static Member from(com.ssafy.trip.domain.member.Member member) {
			return Member.builder()
					.id(member.getId())
					.name(member.getName())
					.role(member.getRole())
					.profileImage(member.getProfileImage())
					.build();
		}
	}
	
	@Getter
	public static class DetailInfo {
		private Info article;
		private SimpleInfo prev;
		private SimpleInfo next;
		
		public DetailInfo(Article article, Article prev, Article next) {
			this.article = Info.from(article);
			this.prev = prev == null ? null : new SimpleInfo(prev);
			this.next = next == null ? null : new SimpleInfo(next);
		}
	}
	
	@Getter
	public static class SimpleInfo {
		private Long id;
		private String title;
		
		public SimpleInfo (Article article) {
			this.id = article.getId();
			this.title = article.getTitle();
		}
	}
	
	
	
}
