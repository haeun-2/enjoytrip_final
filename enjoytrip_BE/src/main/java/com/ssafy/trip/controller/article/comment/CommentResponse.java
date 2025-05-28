package com.ssafy.trip.controller.article.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssafy.trip.domain.article.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class CommentResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Info {
        private Long id;
        private String content;
        private Long memberId;
        private String memberName;
        private String memberProfileImage;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private List<Info> replies;

        public static Info from(Comment comment) {
            return Info.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .memberId(comment.getMember().getId())
                    .memberName(comment.getMember().getName())
                    .memberProfileImage(comment.getMember().getProfileImage())
                    .createdAt(comment.getCreatedAt())
                    .modifiedAt(comment.getModifiedAt())
                    .replies(comment.getReplies().stream()
                            .map(CommentResponse.Info::from)
                            .toList())
                    .build();
        }
    }
}
