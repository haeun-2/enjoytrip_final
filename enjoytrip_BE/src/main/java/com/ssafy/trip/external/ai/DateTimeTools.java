package com.ssafy.trip.external.ai;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DateTimeTools {
    @Tool(description = "현재 날짜와 시간을 반환합니다.")
    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }


}
