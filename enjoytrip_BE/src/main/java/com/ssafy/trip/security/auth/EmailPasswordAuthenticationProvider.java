package com.ssafy.trip.security.auth;

import com.ssafy.trip.common.exception.BaseException;
import com.ssafy.trip.common.exception.ErrorCode;
import com.ssafy.trip.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailPasswordAuthenticationProvider implements AuthenticationProvider {
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        // DB 조회, 존재하지 않는 경우 principal을 null로 설정
        UserDetails user = userDetailsService.loadUserByUsername(username);
        Member member = ((CustomUserDetails) user).getMember();

        if (member.getDeletedAt() != null)
            throw new UsernameNotFoundException("삭제된 사용자입니다.");
        if (member.getPassword() == null)
            throw new BaseException(ErrorCode.INVALID_MEMBER, "비밀번호가 설정되지 않은 사용자입니다.");
        if (!passwordEncoder.matches(password, member.getPassword()))
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");

        return new EmailPasswordAuthenticationToken(user, user.getAuthorities(), user.isAccountNonLocked());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return EmailPasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
