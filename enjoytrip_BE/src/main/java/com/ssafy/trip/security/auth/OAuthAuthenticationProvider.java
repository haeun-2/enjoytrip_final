package com.ssafy.trip.security.auth;

import com.ssafy.trip.domain.auth.OAuthProvider;
import com.ssafy.trip.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuthAuthenticationProvider implements AuthenticationProvider {
    private final CustomUserDetailsService userDetailsService;
    @Override
    public Authentication authenticate(Authentication authentication) {
        OAuthAuthenticationToken token = (OAuthAuthenticationToken) authentication;
        String email = (String) token.getPrincipal();
        String oauthProviderId = (String) token.getCredentials();

        // DB 조회
        UserDetails details = userDetailsService.loadUserByOAuthId(oauthProviderId, email);

        Member member = ((CustomUserDetails) details).getMember();
        if (member != null && member.getDeletedAt() != null)
            throw new UsernameNotFoundException("삭제된 사용자입니다.");

        OAuthProvider oAuthProvider = ((CustomUserDetails) details).getOAuthProvider();
        if (oAuthProvider == null)
            return new OAuthAuthenticationToken(details, null, true);

        return new OAuthAuthenticationToken(details, details.getAuthorities(), details.isAccountNonLocked());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuthAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
