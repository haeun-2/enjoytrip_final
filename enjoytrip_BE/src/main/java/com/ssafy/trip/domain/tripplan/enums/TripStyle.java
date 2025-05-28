package com.ssafy.trip.domain.tripplan.enums;

import lombok.Getter;

@Getter
public enum TripStyle {
    RELAXING("휴양"),
    SIGHTSEEING("관광"),
    NATURE("자연"),
    CULTURAL("문화"),
    FOODIE("미식"),
    ADVENTURE("액티비티"),
    SHOPPING("소펭"),
    PHOTO("사진");

    private final String name;

    TripStyle(String name) {
        this.name = name;
    }
}
