package com.ssafy.trip.domain.attraction;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryDao {
	
	Category select(Long id);

}
