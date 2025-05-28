package com.ssafy.trip.service.tripplan;

import com.ssafy.trip.domain.tripplan.TripPlan;
import com.ssafy.trip.domain.tripplan.TripPlanDao;
import com.ssafy.trip.domain.tripplan.interaction.TripPlanLike;
import com.ssafy.trip.domain.tripplan.interaction.TripPlanLikeDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TripPlanInteractionService {
    private final TripPlanLikeDao tripPlanLikeDao;
    private final TripPlanDao tripPlanDao;

    public void like(long tripPlanId, long memberId) {
        TripPlanLike tripPlanLike = new TripPlanLike(tripPlanId, memberId);
        int exists = tripPlanLikeDao.exists(tripPlanLike);

        if (exists > 0) tripPlanLikeDao.reInsert(tripPlanLike);
        else tripPlanLikeDao.insert(tripPlanLike);

        TripPlan tripPlan = tripPlanDao.selectSimpleById(tripPlanId);
        tripPlan.addLikeCount();
        tripPlanDao.updateLikeCount(tripPlan);
    }

    public void unlike(long tripPlanId, long memberId) {
        TripPlanLike tripPlanLike = new TripPlanLike(tripPlanId, memberId);
        tripPlanLikeDao.delete(tripPlanLike);

        TripPlan tripPlan = tripPlanDao.selectSimpleById(tripPlanId);
        tripPlan.subLikeCount();
        tripPlanDao.updateLikeCount(tripPlan);
    }
}
