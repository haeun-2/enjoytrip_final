package com.ssafy.trip.service.admin.member;

import com.ssafy.trip.common.controller.Page;
import com.ssafy.trip.common.controller.PageRequest;
import com.ssafy.trip.controller.admin.member.AdminMemberResponse;
import com.ssafy.trip.domain.member.Member;
import com.ssafy.trip.domain.member.MemberDao;
import com.ssafy.trip.domain.member.like.MemberSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminMemberService {
    private final MemberDao memberDao;

    public Page<AdminMemberResponse.Info> getAllMembers(PageRequest pageRequest, String status, String keyword) {
        MemberSearchCondition condition = new MemberSearchCondition(pageRequest, keyword, status);
        List<Member> memberList = memberDao.selectAll(condition);
        int count = memberDao.countAll(condition);
        return Page.from(pageRequest, count, memberList, AdminMemberResponse.Info::from);
    }

    public void blockingMember(Long memberId) {
        memberDao.blockingMember(memberId);
    }

    public void unblockingMember(Long memberId) {
        memberDao.unBlockingMember(memberId);
    }

}
