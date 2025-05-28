package com.ssafy.trip.domain.article.interaction;

import com.ssafy.trip.domain.article.Article;
import com.ssafy.trip.domain.article.enums.ReportStatus;
import com.ssafy.trip.domain.member.Member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
public class ArticleReport {
    private Long id;
    private Long articleId;
    private Long memberId;
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime handledAt;
    private ReportStatus status;

    private Article article;
    private Member member;
    
    public ArticleReport(Long articleId, Long memberId, String reason) {
        this.articleId = articleId;
        this.memberId = memberId;
        this.reason = reason;
        this.status = ReportStatus.PENDING;
    }
    
    public void update(ReportStatus status) {
    	this.status = status;
    }
}
