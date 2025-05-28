package com.ssafy.trip.domain.member;

import com.ssafy.trip.domain.member.like.MemberSearchCondition;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberDao {

	int insert(Member member);

	int insertWithoutPassword(Member member);
	
	Member selectById(Long id);
	
	Member selectByEmail(String email);
	
	int update(Member member);

	int updatePassword(Member member);
	
	int softDelete(Member member);

	List<Member> selectAll(MemberSearchCondition condition);

	int countAll(MemberSearchCondition condition);

	int blockingMember(Long memberId);

	int unBlockingMember(Long memberId);

}
