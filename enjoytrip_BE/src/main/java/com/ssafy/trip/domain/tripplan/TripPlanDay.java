package com.ssafy.trip.domain.tripplan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripPlanDay {
    private Long id;
    private LocalDate date;
    private int dayOrder;
    private Long tripPlanId;

    private List<PlanItem> planItems;
}
