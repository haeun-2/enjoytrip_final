package com.ssafy.trip.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(BaseException e, HttpServletRequest request) {
        log.error("[{}] error: {} path: {}, message: {}",
                e.getErrorCode().getCode(),
                e.getErrorCode().name(),
                request.getRequestURI(),
                e.getMessage(),
                e);
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(new ErrorResponse(e.getErrorCode(), e.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(RuntimeException e, HttpServletRequest request) {
        log.error("[시스템 오류] error: {} path: {}, message: {}",
                e.getClass().getSimpleName(),
                request.getRequestURI(),
                e.getMessage(),
                e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage(), request.getRequestURI()));
    }
}
