package com.ssafy.trip.common.exception;

import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class ErrorResponse {
    private final ZonedDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;


    public ErrorResponse(ErrorCode errorCode, String path) {
        this.timestamp = ZonedDateTime.now();
        this.status = errorCode.getHttpStatus().value();
        this.error = errorCode.getHttpStatus().name();
        this.message = errorCode.getMessage();
        this.path = path;
    }

    public ErrorResponse(ErrorCode errorCode, String message, String path) {
        this.timestamp = ZonedDateTime.now();
        this.status = errorCode.getHttpStatus().value();
        this.error = errorCode.getHttpStatus().name();
        this.message = message;
        this.path = path;
    }

    public String toJson() {
        return "{\"timestamp\":\"" + timestamp + "\"" +
                ",\"status\":" + status +
                ",\"error\":\"" + error + "\"" +
                ",\"message\":\"" + message + "\"" +
                ",\"path\":\"" + path + "\"" +
                "}";
    }

}
