package com.ssafy.trip.domain.memberplace.interaction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
public class MemberPlaceLike {
    private Long memberPlaceId;
    private Long memberId;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    public MemberPlaceLike(Long memberPlaceId, Long memberId) {
        this.memberPlaceId = memberPlaceId;
        this.memberId = memberId;
    }
}
