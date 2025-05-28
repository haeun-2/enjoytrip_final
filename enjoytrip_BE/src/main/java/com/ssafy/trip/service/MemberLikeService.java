package com.ssafy.trip.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.trip.common.controller.Page;
import com.ssafy.trip.common.controller.PageRequest;
import com.ssafy.trip.controller.member.MemberLikeResponse;
import com.ssafy.trip.domain.article.Article;
import com.ssafy.trip.domain.attraction.Attraction;
import com.ssafy.trip.domain.member.like.MemberLikeDao;
import com.ssafy.trip.domain.member.like.MemberLikeSearchCondition;
import com.ssafy.trip.domain.memberplace.MemberPlace;
import com.ssafy.trip.domain.tripplan.TripPlan;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberLikeService {
    private final MemberLikeDao mlDao;

    public Page<?> getLikedArticles(Long memberId, PageRequest request) {
        List<Article> list = mlDao.selectArticleLikesByMemberId(new MemberLikeSearchCondition(request, memberId));
        Integer totalCount = mlDao.countArticleLikesByMemberId(memberId);
        return Page.from(request, totalCount, list, MemberLikeResponse.ArticleMemberLike::from);
    }

    public Page<?> getLikedAttractions(Long memberId, PageRequest request) {
        List<Attraction> list = mlDao.selectAttractionLikesByMemberId(new MemberLikeSearchCondition(request, memberId));
        Integer totalCount = mlDao.countAttractionLikesByMemberId(memberId);
        return Page.from(request, totalCount, list, MemberLikeResponse.AttractionMemberLike::from);
    }

    public Page<?> getLikedMemberPlaces(Long memberId, PageRequest request) {
    	List<MemberPlace> list = mlDao.selectMemberPlaceLikesByMemberId(new MemberLikeSearchCondition(request, memberId));
        Integer totalCount = mlDao.countMemberPlaceLikesByMemberId(memberId);
        return Page.from(request, totalCount, list, MemberLikeResponse.MemberPlaceMemberLike::from);
    }

    public Page<?> getLikedTripPlans(Long memberId, PageRequest request) {
    	List<TripPlan> list = mlDao.selectTripPlanLikesByMemberId(new MemberLikeSearchCondition(request, memberId));
        Integer totalCount = mlDao.countTripPlanLikesByMemberId(memberId);
        return Page.from(request, totalCount, list, MemberLikeResponse.TripPlanMemberLike::from);
    }
}
