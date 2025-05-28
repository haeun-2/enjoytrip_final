package com.ssafy.trip.security.filter;

import com.ssafy.trip.common.exception.BaseException;
import com.ssafy.trip.common.exception.ErrorCode;
import com.ssafy.trip.security.auth.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ssafy.trip.domain.member.Member;

import java.io.IOException;

@Component
public class CustomSessionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // authentication 객체가 유효하고, 로그인된 사용자일 경우
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            Member member = userDetails.getMember();

            if (isBlocked(member))
                throw new BaseException(ErrorCode.INVALID_MEMBER, "정지된 사용자입니다. 이메일: "+member.getEmail());
        }

        filterChain.doFilter(request, response);
    }

    private boolean isBlocked(Member member) {
        return member.getBlockedAt() != null;
    }
}
