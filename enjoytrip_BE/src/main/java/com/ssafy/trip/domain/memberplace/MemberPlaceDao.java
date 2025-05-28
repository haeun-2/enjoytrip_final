package com.ssafy.trip.domain.memberplace;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberPlaceDao {
	
	List<MemberPlace> selectAll(MemberPlaceSearchCondition condition);
	
	Long countAll(MemberPlaceSearchCondition condidtion);
	
	MemberPlace select(Long id);
	
	Integer insert(MemberPlace mp);
	
	Integer update(MemberPlace mp);
	
	Integer delete(Long id);

	Integer updateLike(MemberPlace mp);
}
