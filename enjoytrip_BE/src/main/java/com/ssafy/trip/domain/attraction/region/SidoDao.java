package com.ssafy.trip.domain.attraction.region;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SidoDao {
	
	Sido selectByCode(Integer sidoCode);
	
	Sido select(Long id);

	List<Sido> selectAll();

}
