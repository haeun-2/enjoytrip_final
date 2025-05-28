package com.ssafy.trip.domain.article.comment.interaction;

import com.ssafy.trip.domain.article.comment.Comment;
import com.ssafy.trip.domain.article.enums.ReportStatus;
import com.ssafy.trip.domain.member.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentReport {
    private Long id;
    private Long commentId;
    private Long memberId;
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime handledAt;
    private ReportStatus status;
    
    private Comment comment;
    private Member member;
    
    public CommentReport(Long commentId, Long memberId, String reason) {
        this.commentId = commentId;
        this.memberId = memberId;
        this.reason = reason;
        this.status = ReportStatus.PENDING;
    }
    
    public void update(ReportStatus status) {
    	this.status = status;
    }
}
