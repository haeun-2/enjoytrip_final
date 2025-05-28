package com.ssafy.trip.security.auth;

import com.ssafy.trip.domain.auth.OAuthProvider;
import com.ssafy.trip.domain.auth.OauthProviderDao;
import com.ssafy.trip.domain.member.Member;

import com.ssafy.trip.domain.member.MemberDao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberDao memberDao;
    private final OauthProviderDao oauthProviderDao;

    // username(email)로 사용자를 찾는 메서드
    @Override
    public UserDetails loadUserByUsername(String username){
        Member member = memberDao.selectByEmail(username);
        if (member == null)
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        return new CustomUserDetails(null, member);
    }

    // oauth provider key로 사용자를 찾는 메서드
    public UserDetails loadUserByOAuthId(String oAuthProviderId, String email) throws UsernameNotFoundException {
        OAuthProvider oAuthProvider = oauthProviderDao.selectByOAuthProviderId(oAuthProviderId);
        if (oAuthProvider != null)
            return new CustomUserDetails(oAuthProvider, oAuthProvider.getMember());

        Member member = memberDao.selectByEmail(email);
        if (member != null)
            return new CustomUserDetails(null, member);

        // OAuthProvider가 null인 경우는 처음 로그인 시도
        return new CustomUserDetails(null, null);
    }
}
