package com.ssafy.trip.domain.article.interaction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
public class ArticleLike {
    private Long articleId;
    private Long memberId;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    public ArticleLike(Long articleId, Long memberId) {
        this.articleId = articleId;
        this.memberId = memberId;
    }
}
