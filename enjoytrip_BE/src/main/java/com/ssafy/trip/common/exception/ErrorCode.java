package com.ssafy.trip.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /**
     * 대문자로 ErrorCode 정의 (HttpStatus / 메세지 / 식별 코드 [도메인앞글자 + 번호])
     */

    // Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다.", "M001"),
    MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 회원입니다.", "M002"),
    MEMBER_BLOCKED(HttpStatus.UNAUTHORIZED, "정지된 회원입니다.", "M003"),
    // Auth
    INVALID_MEMBER(HttpStatus.UNAUTHORIZED, "사용이 불가능한 회원입니다.", "A001"),
    UNMATCHED_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.", "A002"),
    
    // Attraction
    ATTRACTION_NOT_FOUND(HttpStatus.NOT_FOUND, "관광정보가 존재하지 않습니다.", "AT001"),
    
    // MemberPlace
    MEMBERPLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "핫플정보가 존재하지 않습니다.", "MP001"),
    
    // Article
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다.", "AC001"),
    ARTICLE_AUTHENTICATION_FAILED(HttpStatus.FORBIDDEN, "게시글 접근 권한이 없습니다.", "AC002"),

    // Comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다.", "C001"),
    
    // Sido
    SIDO_NOT_FOUND(HttpStatus.NOT_FOUND, "시도 정보가 존재하지 않습니다.", "S001"),
    
    // Gugun
    GUGUN_NOT_FOUND(HttpStatus.NOT_FOUND, "구군 정보가 존재하지 않습니다.", "G001"),
    
    // Category
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리 정보가 존재하지 않습니다.", "CT001"),
    

    // TripPlan
    TRIPPLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "여행계획이 존재하지 않습니다.", "TP001"),
    TRIPPLAN_AUTHENTICATION_FAILED(HttpStatus.FORBIDDEN, "여행계획 접근 권한이 없습니다.", "TP002"),
    TRIPPLAN_INVALID_DATE(HttpStatus.BAD_REQUEST, "여행 계획 날짜가 유효하지 않습니다.", "TP003"),

    // System
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.", "S001");

    private final HttpStatus httpStatus;
    private final String message;
    private final String code;
}
