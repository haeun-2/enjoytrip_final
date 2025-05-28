package com.ssafy.trip.common.controller;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageRequest {
    private final int page;
    private final int size;

    @Hidden
    public int getOffset() {
        return page * size;
    }
}
