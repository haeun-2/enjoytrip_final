package com.ssafy.trip.member;

import com.ssafy.trip.domain.auth.enums.Role;
import com.ssafy.trip.domain.member.Member;
import com.ssafy.trip.domain.member.MemberDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MybatisTest
public class MemberDaoTest {

    @Autowired
    private MemberDao memberDao;

    @Test
    @DisplayName("insert 테스트")
    void insertTest() {
        // given
        Member member = createMember();
        // when
        int result = memberDao.insert(member);
        // then
        assertEquals(1, result);
        assertNotNull(member.getId());
    }

    @Test
    @DisplayName("select by id 테스트")
    void selectByIdTest() {
        // given
        Member member = createMember();
        memberDao.insert(member);
        Long memberId = member.getId();

        // when
        Member selectedMember = memberDao.selectById(memberId);

        // then
        assertNotNull(selectedMember);
        assertEquals(memberId, selectedMember.getId());
        assertEquals(member.getEmail(), selectedMember.getEmail());
    }

    @Test
    @DisplayName("select by email 테스트")
    void selectByEmailTest() {
        // given
        Member member = createMember();
        memberDao.insert(member);
        String email = member.getEmail();

        // when
        Member selectedMember = memberDao.selectByEmail(email);

        // then
        assertNotNull(selectedMember);
        assertEquals(email, selectedMember.getEmail());
    }

    @Test
    @DisplayName("update 테스트")
    void updateTest() {
        // given
        Member member = createMember();
        memberDao.insert(member);
        String newName = "수정된 닉네임";
        String newProfileImage = "updated.jpg";
        LocalDate newBirthDate = member.getBirthDate().plusDays(10);

        // when
        member.update(newName, newProfileImage, newBirthDate);
        int result = memberDao.update(member);

        // then
        assertEquals(1, result);
        Member updatedMember = memberDao.selectById(member.getId());
        assertEquals(newName, updatedMember.getName());
        assertEquals(newProfileImage, updatedMember.getProfileImage());
        assertEquals(newBirthDate, updatedMember.getBirthDate());
        assertEquals(member.getEmail(), updatedMember.getEmail()); // 이메일은 수정되지 않아야 함
    }

    @Test
    @DisplayName("delete 테스트")
    void deleteTest() {
        // given
        Member member = createMember();
        memberDao.insert(member);
        Long memberId = member.getId();

        // when
        int result = memberDao.softDelete(member);

        // then
        assertEquals(1, result);
        Member deletedMember = memberDao.selectById(memberId);
        assertNotNull(deletedMember); // member는 여전히 존재해야 함
        assertNotNull(deletedMember.getDeletedAt());
    }

    private Member createMember() {
        return Member.builder()
                .email("test@email.com")
                .password("1234")
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .name("테스트")
                .birthDate(LocalDateTime.now().toLocalDate())
                .profileImage("test.jpg")
                .build();
    }
}