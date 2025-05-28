package com.ssafy.trip.domain.attraction.region;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GugunDao {

	Gugun selectByCode(Integer sidoCode, Integer gugunCode);

	Gugun select(Long id);

	List<Gugun> selectAllBySido(Integer sidoId);

	List<Gugun> selectAllBySidoCode(Long sidoCode);

}
