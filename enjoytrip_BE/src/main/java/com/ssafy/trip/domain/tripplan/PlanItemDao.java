package com.ssafy.trip.domain.tripplan;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PlanItemDao {
	int insert(PlanItem planItem);
	
	int insertRoute(RouteConnection routeConnection);

	int deleteAllByIds(List<Long> ids);

}
