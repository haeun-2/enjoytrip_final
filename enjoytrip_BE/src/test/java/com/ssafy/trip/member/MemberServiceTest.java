package com.ssafy.trip.member;

import com.ssafy.trip.controller.member.MemberRequest;
import com.ssafy.trip.controller.member.MemberResponse;
import com.ssafy.trip.domain.auth.enums.Role;
import com.ssafy.trip.domain.member.Member;
import com.ssafy.trip.domain.member.MemberDao;
import com.ssafy.trip.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @Mock
    private MemberDao memberDao;

    @InjectMocks
    private MemberService memberService;


    @Test
    @DisplayName("로그인된 사용자의 정보에 접근하는 경우 get 테스트")
    void successGetTest() {
        // given
        Long memberId = 1L;
        Long loginUserId = 1L;
        Member member = createMember("user@email.com");

        when(memberDao.selectById(memberId)).thenReturn(member);

        // when
        MemberResponse.Info result = memberService.get(memberId, loginUserId);

        // then
        assertThat(result.getName()).isEqualTo(member.getName());
    }

    @Test
    @DisplayName("본인 외의 다른 사용자 정보에 접근하는 경우 get 테스트")
    void forbiddenGetTest() {
        // given
        Long memberId = 1L;
        Long loginUserId = 2L;
        Member member = createMember("my@email.com");

        lenient().when(memberDao.selectById(memberId)).thenReturn(member);

        // when & then
        assertThrows(AuthorizationDeniedException.class, () -> {
            memberService.get(memberId, loginUserId);
        });
    }

    @Test
    @DisplayName("회원 정보가 존재하지 않는 경우 get 테스트")
    void notFoundGetTest() {
        // given
        Long memberId = 999L;
        Long loginUserId = 999L;

        when(memberDao.selectById(memberId)).thenReturn(null);

        // when & then
        assertThrows(NullPointerException.class, () -> {
            memberService.get(memberId, loginUserId);
        });
    }

    @Test
    @DisplayName("다른 사람의 정보에 접근한 경우, 수정이 안되는지 테스트")
    void notUpdatedWhenAccessOther() {
        // given
        Long memberId = 1L;
        Long loginUserId = 2L;
        Member member = createMember("my@email.com");
        MemberRequest.Update updateRequest = new MemberRequest.Update("수정된 이름", null, null);

        lenient().when(memberDao.selectById(memberId)).thenReturn(member);

        // when + then
        assertThrows(AuthorizationDeniedException.class, () -> {
            memberService.update(memberId, updateRequest, loginUserId);
        });

        // verify: update 호출이 없었는지 검증
        verify(memberDao, never()).update(any());
    }

    @Test
    @DisplayName("회원 탈퇴 테스트")
    void deleteTest() {
        // given
        Long memberId = 1L;
        Long loginUserId = 1L;
        Member member = createMember("user@email.com");

        when(memberDao.selectById(memberId)).thenReturn(member);

        // when
        memberService.delete(memberId, loginUserId);

        // then
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    private Member createMember(String email) {
        return Member.builder()
                .email(email)
                .password("1234")
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .name("테스트")
                .birthDate(LocalDateTime.now().toLocalDate())
                .profileImage("test.jpg")
                .build();
    }
}
