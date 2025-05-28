package com.ssafy.trip.domain.tripplan.enums;

import java.time.LocalDate;

public enum Season {
    SPRING, SUMMER, FALL, WINTER;

    public static Season from(LocalDate date) {
        return switch (date.getMonthValue()) {
            case 3, 4, 5 -> SPRING;
            case 6, 7, 8 -> SUMMER;
            case 9, 10, 11 -> FALL;
            default -> WINTER;
        };
    }
}