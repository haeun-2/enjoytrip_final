package com.ssafy.trip.service.memberplace;

import com.ssafy.trip.domain.memberplace.MemberPlace;
import com.ssafy.trip.domain.memberplace.MemberPlaceDao;
import com.ssafy.trip.domain.memberplace.interaction.MemberPlaceLike;
import com.ssafy.trip.domain.memberplace.interaction.MemberPlaceLikeDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberPlaceInteractionService {
    private final MemberPlaceLikeDao memberPlaceLikeDao;
    private final MemberPlaceDao memberPlaceDao;

    @Transactional
    public void like(long memberPlaceId, long memberId) {
        MemberPlaceLike memberPlaceLike = new MemberPlaceLike(memberPlaceId, memberId);
        memberPlaceLikeDao.insert(memberPlaceLike);
        MemberPlace memberPlace = memberPlaceDao.select(memberPlaceId);
        memberPlace.addLikeCount();
        memberPlaceDao.updateLike(memberPlace);
    }

    @Transactional
    public void unlike(long memberPlaceId, long memberId) {
        MemberPlaceLike memberPlaceLike = new MemberPlaceLike(memberPlaceId, memberId);
        memberPlaceLikeDao.delete(memberPlaceLike);
        MemberPlace memberPlace = memberPlaceDao.select(memberPlaceId);
        memberPlace.subLikeCount();
        memberPlaceDao.updateLike(memberPlace);
    }
}
