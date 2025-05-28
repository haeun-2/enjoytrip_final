package com.ssafy.trip.domain.tripplan.interaction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
public class TripPlanLike {
    private long tripPlanId;
    private long memberId;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    public TripPlanLike(long tripPlanId, long memberId) {
        this.tripPlanId = tripPlanId;
        this.memberId = memberId;
    }
}
