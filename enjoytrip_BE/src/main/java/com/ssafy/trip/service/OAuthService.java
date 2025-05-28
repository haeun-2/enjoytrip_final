package com.ssafy.trip.service;

import com.ssafy.trip.common.exception.BaseException;
import com.ssafy.trip.common.exception.ErrorCode;
import com.ssafy.trip.domain.auth.OAuthProvider;
import com.ssafy.trip.domain.auth.OauthProviderDao;
import com.ssafy.trip.domain.member.Member;
import com.ssafy.trip.domain.member.MemberDao;
import com.ssafy.trip.external.oauth.KakaoOAuthClient;
import com.ssafy.trip.external.oauth.NaverOAuthClient;
import com.ssafy.trip.security.auth.OAuthAuthenticationToken;
import com.ssafy.trip.external.oauth.dto.KakaoUser;
import com.ssafy.trip.external.oauth.dto.NaverUser;
import com.ssafy.trip.security.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {
    private final MemberDao memberDao;
    private final KakaoOAuthClient kakaoClient;
    private final NaverOAuthClient naverClient;
    private final OauthProviderDao oauthProviderDao;
    private final AuthenticationManager authenticationManager;

    public void kakaoOAuth(String code) {
        String accessToken = kakaoClient.getAccessToken(code);
        KakaoUser kakaoUser = kakaoClient.getUserInfo(accessToken);
        authenticateOAuthUser(OAuthProvider.from(kakaoUser));
    }

    public void naverOAuth(String code, String state) {
        String accessToken = naverClient.getAccessToken(code, state);
        NaverUser naverUser = naverClient.getUserInfo(accessToken);
        authenticateOAuthUser(OAuthProvider.from(naverUser));
    }

    private void authenticateOAuthUser(OAuthProvider oAuthUser) {
        // oauth 인증 토큰 생성
        OAuthAuthenticationToken OAuthAuthenticationToken =
                new OAuthAuthenticationToken(oAuthUser.getKey(), oAuthUser.getMember().getEmail());

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(OAuthAuthenticationToken);

        } catch (LockedException e) {
            throw new BaseException(ErrorCode.INVALID_MEMBER, "정지된 사용자입니다. 이메일: " + oAuthUser.getMember().getEmail());
        } catch (BaseException | BadCredentialsException | UsernameNotFoundException e) {
            throw new BaseException(ErrorCode.INVALID_MEMBER, e.getMessage());
        }

        OAuthProvider oAuthProvider = ((CustomUserDetails) authentication.getPrincipal()).getOAuthProvider();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();

        if (oAuthProvider == null && member == null) {
            // 회원가입 처리
            memberDao.insertWithoutPassword(oAuthUser.getMember());
            oAuthUser.setMemberId(oAuthUser.getMember().getId());
            oauthProviderDao.insert(oAuthUser);
            // 회원가입 후 다시 인증 시도
            authenticateOAuthUser(oAuthUser);
            return;
        }
        if (oAuthProvider == null && member != null) {
            // 기존 계정과 oauth provider 연결
            oAuthUser.setMemberId(member.getId());
            oauthProviderDao.insert(oAuthUser);
            // 회원가입 후 다시 인증 시도
            authenticateOAuthUser(oAuthUser);
            return;
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // 인증 성공 → SecurityContext 에 등록
        authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
