package com.ssafy.trip.domain.attraction.interaction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
public class AttractionLike {
    private long attractionId;
    private long memberId;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    public AttractionLike(long attractionId, long memberId) {
        this.attractionId = attractionId;
        this.memberId = memberId;
    }
}
