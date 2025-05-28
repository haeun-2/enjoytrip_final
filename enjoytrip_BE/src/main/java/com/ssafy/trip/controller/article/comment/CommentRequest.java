package com.ssafy.trip.controller.article.comment;

import com.ssafy.trip.domain.article.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class CommentRequest {

    @Getter
    @AllArgsConstructor
    public static class Create {
        private Long parentId;
        private String content;

        public Comment toEntity(Long memberId, Long articleId) {
            return Comment.builder()
                    .parentId(parentId)
                    .content(content)
                    .memberId(memberId)
                    .articleId(articleId)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Update {
        private String content;
    }
}
