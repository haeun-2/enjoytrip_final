package com.ssafy.trip.domain.attraction.interaction;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AttractionLikeDao {
    int insert(AttractionLike attractionLike);

    int delete(AttractionLike attractionLike);

    int exists(AttractionLike attractionLike);

    int reInsert(AttractionLike attractionLike);
}
