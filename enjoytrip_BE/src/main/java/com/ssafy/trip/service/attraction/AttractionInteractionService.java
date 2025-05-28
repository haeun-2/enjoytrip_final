package com.ssafy.trip.service.attraction;

import com.ssafy.trip.domain.attraction.Attraction;
import com.ssafy.trip.domain.attraction.AttractionDao;
import com.ssafy.trip.domain.attraction.AttractionSearchCondition;
import com.ssafy.trip.domain.attraction.interaction.AttractionLike;
import com.ssafy.trip.domain.attraction.interaction.AttractionLikeDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttractionInteractionService {
    private final AttractionLikeDao attractionLikeDao;
    private final AttractionDao attractionDao;


    public void like(long attractionId, long memberId) {
        AttractionLike attractionLike = new AttractionLike(attractionId, memberId);
        int result = attractionLikeDao.exists(attractionLike);
        if (result > 0) attractionLikeDao.reInsert(attractionLike);
        else attractionLikeDao.insert(attractionLike);

        Attraction attraction = attractionDao.selectSummaryById(attractionId);
        attraction.addLikeCount();
        attractionDao.updateLikeCount(attraction);
    }

    public void unlike(long attractionId, long memberId) {
        AttractionLike attractionLike = new AttractionLike(attractionId, memberId);
        attractionLikeDao.delete(attractionLike);

        Attraction attraction = attractionDao.selectSummaryById(attractionId);
        attraction.subLikeCount();
        attractionDao.updateLikeCount(attraction);
    }

}
