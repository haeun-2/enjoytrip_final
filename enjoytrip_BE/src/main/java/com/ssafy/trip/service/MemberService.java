package com.ssafy.trip.service;

import com.ssafy.trip.controller.member.MemberResponse;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.trip.common.exception.BaseException;
import com.ssafy.trip.common.exception.ErrorCode;
import com.ssafy.trip.controller.member.MemberRequest;
import com.ssafy.trip.domain.member.Member;
import com.ssafy.trip.domain.member.MemberDao;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	public final MemberDao memberDao;
	public final PasswordEncoder passwordEncoder;
	
	public MemberResponse.Info get(Long memberId, Long loginUserId) {
		if (!memberId.equals(loginUserId)) {
			throw new AuthorizationDeniedException("본인의 정보에만 접근할 수 있습니다.");
		}
		Member profileMember = memberDao.selectById(memberId);
		return MemberResponse.Info.from(profileMember);
	}
	
	public void update(Long memberId, MemberRequest.Update request, Long loginUserId) {
		if (!memberId.equals(loginUserId)) {
			throw new AuthorizationDeniedException("본인의 정보에만 접근할 수 있습니다.");
		}
		Member member = memberDao.selectById(memberId);

		member.update(request.getName(), request.getProfileImage(), request.getBirthDate());
		memberDao.update(member);
	}

	public void updatePassword(Long memberId, MemberRequest.UpdatePassword request, Long loginUserId) {
		if (!memberId.equals(loginUserId)) {
			throw new AuthorizationDeniedException("본인의 정보에만 접근할 수 있습니다.");
		}
		Member member = memberDao.selectById(memberId);

		if (!passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
            throw new BaseException(ErrorCode.UNMATCHED_PASSWORD);
		}
		
		member.encodePassword(passwordEncoder.encode(request.getNewPassword()));
		memberDao.updatePassword(member);
	}
	
	public void delete(Long memberId, Long loginUserId){
		if (!memberId.equals(loginUserId)) {
			throw new AuthorizationDeniedException("본인의 정보에만 접근할 수 있습니다.");
		}
		Member member = memberDao.selectById(memberId);

		memberDao.softDelete(member);
		SecurityContextHolder.getContext().setAuthentication(null);
	}
}
