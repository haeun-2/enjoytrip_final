package com.ssafy.trip.controller.admin.article;

import java.time.LocalDateTime;

import com.ssafy.trip.domain.article.Article;
import com.ssafy.trip.domain.article.comment.interaction.CommentReport;
import com.ssafy.trip.domain.article.enums.ArticleType;
import com.ssafy.trip.domain.article.enums.ReportStatus;
import com.ssafy.trip.domain.article.interaction.ArticleReport;
import com.ssafy.trip.domain.article.qna.ArticleAnswer;
import com.ssafy.trip.domain.article.qna.QnaAnswer;
import com.ssafy.trip.domain.auth.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class AdminArticleResponse {

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
					.build();
		}
		
	}
	
	@Builder
	@AllArgsConstructor
	@Getter
	public static class ReportInfo {
		private Long id;
		private String targetType;
	    private Long targetId;
	    private String targetName;
	    private Long memberId;
	    private String memberName;
	    private String reason;
	    private LocalDateTime createdAt;
	    private LocalDateTime handledAt;
	    private ReportStatus status;
	    
	    public static ReportInfo from(ArticleReport report) {
	    	return ReportInfo.builder()
	    				.id(report.getId())
	    				.targetType("Article")
	    				.targetId(report.getId())
	    				.targetName(report.getArticle().getTitle())
	    				.memberId(report.getMemberId())
	    				.memberName(report.getMember().getName())
	    				.reason(report.getReason())
	    				.createdAt(report.getCreatedAt())
	    				.handledAt(report.getHandledAt())
	    				.status(report.getStatus())
	    				.build();
	    }
	    
	    public static ReportInfo from(CommentReport report) {
	    	return ReportInfo.builder()
	    			.id(report.getId())
	    			.targetType("Comment")
    				.targetId(report.getId())
    				.targetName(report.getComment().getContent())
    				.memberId(report.getMemberId())
    				.memberName(report.getMember().getName())
    				.reason(report.getReason())
    				.createdAt(report.getCreatedAt())
    				.handledAt(report.getHandledAt())
    				.status(report.getStatus())
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

}
