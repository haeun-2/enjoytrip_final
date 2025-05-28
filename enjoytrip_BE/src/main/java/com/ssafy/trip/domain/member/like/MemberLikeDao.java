package com.ssafy.trip.domain.member.like;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.trip.domain.article.Article;
import com.ssafy.trip.domain.attraction.Attraction;
import com.ssafy.trip.domain.memberplace.MemberPlace;
import com.ssafy.trip.domain.tripplan.TripPlan;

@Mapper
public interface MemberLikeDao {
	List<Article> selectArticleLikesByMemberId(MemberLikeSearchCondition condition);
	
	Integer countArticleLikesByMemberId(Long memberId);
	
    List<Attraction> selectAttractionLikesByMemberId(MemberLikeSearchCondition condition);
    
	Integer countAttractionLikesByMemberId(Long memberId);
    
    List<MemberPlace> selectMemberPlaceLikesByMemberId(MemberLikeSearchCondition condition);
    
	Integer countMemberPlaceLikesByMemberId(Long memberId);
    
    List<TripPlan> selectTripPlanLikesByMemberId(MemberLikeSearchCondition condition);
    
	Integer countTripPlanLikesByMemberId(Long memberId);

}
