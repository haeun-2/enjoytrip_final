package com.ssafy.trip.domain.tripplan;

import com.ssafy.trip.domain.tripplan.dto.TripPlanStyleDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TripPlanDao {

    List<TripPlan> selectAll(TripPlanSearchCondition condition);

    List<TripPlan> selectAllMine(TripPlanSearchCondition condition);

    int countAll(TripPlanSearchCondition condition);

    int countAllMine(TripPlanSearchCondition condition);

    TripPlan selectSimpleById(Long id);

    TripPlan selectById(Long id);

    void insert(TripPlan tripPlan);

    void update(TripPlan tripPlan);

    void delete(Long tripPlanId);

    void insertTripPlanDays(List<TripPlanDay> tripPlanDay);

    void insertTripStyles(List<TripPlanStyleDto> tripPlanStyles);

    Long selectMostLikedId();

    void updateTripPlanDays(List<TripPlanDay> tripPlanDay);

    void deleteTripPlanDays(List<Long> tripPlanDayId);

    void updateLikeCount(TripPlan tripPlan);

    void updateViewCount(TripPlan tripPlan);
}