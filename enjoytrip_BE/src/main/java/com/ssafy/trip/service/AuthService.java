package com.ssafy.trip.service;

import com.ssafy.trip.common.exception.BaseException;
import com.ssafy.trip.common.exception.ErrorCode;
import com.ssafy.trip.controller.auth.AuthRequest;
import com.ssafy.trip.controller.auth.AuthResponse;
import com.ssafy.trip.domain.member.Member;
import com.ssafy.trip.domain.member.MemberDao;
import com.ssafy.trip.security.auth.CustomUserDetails;
import com.ssafy.trip.security.auth.EmailPasswordAuthenticationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberDao memberDao;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * 이메일, 비밀번호로 회원가입
     */
    public AuthResponse.SignIn singIn(AuthRequest.SignIn request) {
        Member member = request.toEntity();
        member.encodePassword(passwordEncoder.encode(member.getPassword()));
        try {
            memberDao.insert(member);
        } catch (DuplicateKeyException e) {
            throw new BaseException(ErrorCode.MEMBER_ALREADY_EXISTS);
        }
        return AuthResponse.SignIn.from(member);
    }


    /**
     * 이메일, 비밀번호로 로그인
     */
    public AuthResponse.Login.Data login(AuthRequest.Login request) throws AuthenticationException {
        // 이메일과 비밀번호로 인증 토큰 생성
        EmailPasswordAuthenticationToken authenticationToken =
                new EmailPasswordAuthenticationToken(request.getEmail(), request.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            // 인증 성공 시 SecurityContext에 인증 정보 저장
            authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return AuthResponse.Login.Data.from(userDetails.getMember());

        } catch (LockedException e) {
            throw new BaseException(ErrorCode.MEMBER_BLOCKED, "정지된 사용자입니다. 이메일: " + request.getEmail());
        } catch (BaseException | BadCredentialsException | UsernameNotFoundException e) {
            throw new BaseException(ErrorCode.INVALID_MEMBER, e.getMessage());
        }
    }
}
