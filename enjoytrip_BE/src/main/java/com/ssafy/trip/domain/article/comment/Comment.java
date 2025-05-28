package com.ssafy.trip.domain.article.comment;

import com.ssafy.trip.domain.member.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private Long id;
    private Long parentId;
    private String content;
    private Long memberId;
    private Long articleId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;

    private List<Comment> replies = new ArrayList<>();
    private Member member;
    public void updateContent(String content) {
        this.content = content;
    }
}
