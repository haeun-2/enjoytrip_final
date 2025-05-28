package com.ssafy.trip.domain.memberplace.interaction;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberPlaceLikeDao {
    int insert(MemberPlaceLike memberPlaceLike);

    int delete(MemberPlaceLike memberPlaceLike);
}
