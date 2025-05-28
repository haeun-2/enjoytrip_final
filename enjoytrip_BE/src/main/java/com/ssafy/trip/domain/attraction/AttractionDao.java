package com.ssafy.trip.domain.attraction;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AttractionDao {
	List<Attraction> getListWithPaging(AttractionSearchCondition condition);

	List<Attraction> getListNearby(AttractionSearchCondition condition);

	int getTotalCount(AttractionSearchCondition condition);

	List<Attraction> search(AttractionSearchCondition condition);

	Attraction select(AttractionSearchCondition condition);

	Attraction selectSummaryById(long id);

	List<Attraction> selectAllName();

	Integer updateLikeCount(Attraction attraction);
	
}
