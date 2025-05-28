package com.ssafy.trip.service.tripplan;

import com.ssafy.trip.common.controller.Page;
import com.ssafy.trip.common.controller.PageRequest;
import com.ssafy.trip.common.exception.BaseException;
import com.ssafy.trip.common.exception.ErrorCode;
import com.ssafy.trip.controller.tripplan.PlanRequest;
import com.ssafy.trip.controller.tripplan.PlanRequest.CreateDetail.TripPlanDayInfo.ItemInfo;
import com.ssafy.trip.controller.tripplan.PlanResponse;
import com.ssafy.trip.domain.tripplan.PlanItem;
import com.ssafy.trip.domain.tripplan.PlanItemDao;
import com.ssafy.trip.domain.tripplan.RouteConnection;
import com.ssafy.trip.domain.tripplan.TripPlan;
import com.ssafy.trip.domain.tripplan.TripPlanDao;
import com.ssafy.trip.domain.tripplan.TripPlanDay;
import com.ssafy.trip.domain.tripplan.TripPlanSearchCondition;
import com.ssafy.trip.domain.tripplan.dto.TripPlanStyleDto;
import com.ssafy.trip.domain.tripplan.enums.Season;
import com.ssafy.trip.domain.tripplan.enums.TripStyle;
import com.ssafy.trip.external.ai.AiClient;
import com.ssafy.trip.security.auth.util.AuthenticationUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripPlanService {
    private final TripPlanDao tripPlanDao;
    private final PlanItemDao planItemDao;
    private final AiClient aiClient;

    public Page<PlanResponse.Summary> getAllTripPlans(PlanRequest.SearchCondition condition, PageRequest pageRequest, Authentication authentication) {
        Long loginMemberId = AuthenticationUserUtil.getUserId(authentication);
        Long userId = condition.getMine() != null ? loginMemberId : null;
        TripPlanSearchCondition searchCondition = condition.toCondition(userId, loginMemberId, pageRequest);
        
        List<TripPlan> tripPlans = tripPlanDao.selectAll(searchCondition);
        int totalCount = tripPlanDao.countAll(searchCondition);
        return Page.from(pageRequest, totalCount, tripPlans, PlanResponse.Summary::from);
    }

    public Page<PlanResponse.Summary> getPlansWithStatus(String status, PageRequest pageRequest, Long memberId) {
        TripPlanSearchCondition condition = TripPlanSearchCondition.builder()
                .offset(pageRequest.getOffset())
                .size(pageRequest.getSize())
                .memberId(memberId)
                .status(status)
                .now(LocalDate.now())
                .build();
        List<TripPlan> tripPlans = tripPlanDao.selectAllMine(condition);
        int totalCount = tripPlanDao.countAllMine(condition);
        return Page.from(pageRequest, totalCount, tripPlans, PlanResponse.Summary::from);
    }
    
    public PlanResponse.Summary create(PlanRequest.Create request, Long memberId) {
    	TripPlan plan = calculateTripPlanDate(request);
    	plan.setAuthor(memberId);
    	tripPlanDao.insert(plan);
    	return PlanResponse.Summary.from(plan);
    }

    /**
     * 여행 계획 날짜 계산 함수
     * 여행 총 일수가 100일을 넘는 경우 계절을 null로 설정
     */
    private TripPlan calculateTripPlanDate(PlanRequest.Create request) {
        int totalDays = (int) (request.getEndDate().toEpochDay() - request.getStartDate().toEpochDay()) + 1;
        if (totalDays <= 0) {
            throw new BaseException(ErrorCode.TRIPPLAN_INVALID_DATE);
        }
        Season season = (totalDays < 100) ? Season.from(request.getStartDate()) : null;
        return request.toEntity(totalDays, season);
    }
    
    
    
    @Transactional
    public void createDetail(PlanRequest.CreateDetail request, Long tripPlanId) {
        StringBuffer tripContent = new StringBuffer();

        TripPlan tripPlan = updateTripPlan(request, tripPlanId, tripContent);
        List<TripPlanDay> tripPlanDays = saveTripPlanDays(request.getTripPlanDayInfo(), tripPlan.getId());
        savePlanItemsWithRoutes(request.getTripPlanDayInfo(), tripPlanDays, tripContent);

        List<TripStyle> tripStyles = generateTripStyle(tripContent.toString());
//        List<TripStyle> tripStyles = List.of(TripStyle.CULTURAL, TripStyle.PHOTO);
        saveTripPlanStyles(tripStyles, tripPlan.getId());
    }

    private List<TripStyle> generateTripStyle(String tripContent) {
        return aiClient.tripStyleGeneration(tripContent);
    }

    private void saveTripPlanStyles(List<TripStyle> tripStyles, Long tripPlanId) {
        if (tripStyles != null && !tripStyles.isEmpty()) {
            List<TripPlanStyleDto> tripPlanStyleDto = tripStyles.stream()
                .map(tripStyle -> new TripPlanStyleDto(tripPlanId, tripStyle.name()))
                .toList();

            tripPlanDao.insertTripStyles(tripPlanStyleDto);
        }
    }

    private TripPlan updateTripPlan(PlanRequest.CreateDetail request, Long tripPlanId, StringBuffer tripContent) {
        TripPlan tripPlan = request.toEntity(tripPlanId);
        tripPlanDao.update(tripPlan);
        tripContent.append("여행제목: ").append(tripPlan.getTitle()).append("\n");
        tripContent.append("여행설명: ").append(tripPlan.getDescription()).append("\n");
        tripContent.append("계절: ").append(tripPlan.getSeason()).append("\n");
        tripContent.append("여행지 카테고리 목록: ");
        return tripPlan;
    }

    private List<TripPlanDay> saveTripPlanDays(List<PlanRequest.CreateDetail.TripPlanDayInfo> dayInfos, Long tripPlanId) {
        List<TripPlanDay> tripPlanDays = dayInfos.stream()
            .map(info -> info.toEntity(tripPlanId))
            .toList();

        tripPlanDao.insertTripPlanDays(tripPlanDays);
        return tripPlanDays;
    }

    private void savePlanItemsWithRoutes(
        List<PlanRequest.CreateDetail.TripPlanDayInfo> dayInfos,
        List<TripPlanDay> tripPlanDays,
        StringBuffer tripContent
    ) {
        for (int i = 0; i < dayInfos.size(); i++) {
            PlanRequest.CreateDetail.TripPlanDayInfo dayInfo = dayInfos.get(i);
            Long tripPlanDayId = tripPlanDays.get(i).getId();
            saveItemsForDay(dayInfo.getItems(), tripPlanDayId, tripContent);
        }
    }

    private void saveItemsForDay(List<ItemInfo> items, Long tripPlanDayId, StringBuffer tripContent) {
        Long prevId = null;
        for (ItemInfo itemInfo : items) {
            PlanItem planItem = itemInfo.toEntity(tripPlanDayId, itemInfo.getAttractionId(), itemInfo.getMemberPlaceId());
            planItemDao.insert(planItem);
            tripContent.append(planItem.getItemType()).append(", ");
            if (prevId != null && itemInfo.getRouteFromPrevious() != null) {
                RouteConnection routeConnection = itemInfo.getRouteFromPrevious().toEntity(prevId, planItem.getId());
                planItemDao.insertRoute(routeConnection);
            }

            prevId = planItem.getId();
        }
    }

    @Transactional
    public void update(PlanRequest.CreateDetail request, Long tripPlanId, Long memberId) {
        // 이전 정보 삭제
        TripPlan oldTripPlan = deleteForUpdate(request, tripPlanId);
        if (!oldTripPlan.getMemberId().equals(memberId)) {
            throw new BaseException(ErrorCode.TRIPPLAN_AUTHENTICATION_FAILED);
        }
        // 새로운 정보 저장
        StringBuffer tripContent = new StringBuffer();
        updateTripPlan(request, tripPlanId, tripContent);
        List<TripPlanDay> tripPlanDays = oldTripPlan.getTripPlanDays();
        savePlanItemsWithRoutes(request.getTripPlanDayInfo(), tripPlanDays, tripContent);
    }

    public TripPlan deleteForUpdate(PlanRequest.CreateDetail request, Long tripPlanId) {
        // 이전 정보 삭제
        TripPlan oldTripPlan = tripPlanDao.selectById(tripPlanId);
        List<Long> tripItemIds = oldTripPlan.getTripPlanDays().stream()
                .flatMap(tripPlanDay -> tripPlanDay.getPlanItems().stream())
                .map(PlanItem::getId)
                .toList();
        planItemDao.deleteAllByIds(tripItemIds);
        return oldTripPlan;
    }
    
    public PlanResponse.Info getDetail(Long id) {
    	TripPlan tripPlan = tripPlanDao.selectById(id);
        tripPlan.updateViewCount();
        tripPlanDao.updateViewCount(tripPlan);
    	return PlanResponse.Info.from(tripPlan);
    }

    public PlanResponse.Info getTrendingDetail() {
        Long tripPlanId = tripPlanDao.selectMostLikedId();
        TripPlan tripPlan = tripPlanDao.selectById(tripPlanId);
        return PlanResponse.Info.from(tripPlan);
    }

    public void delete(Long tripPlanId, Long memberId) {
        TripPlan tripPlan = tripPlanDao.selectSimpleById(tripPlanId);
        if (!tripPlan.getMemberId().equals(memberId)) {
            throw new BaseException(ErrorCode.TRIPPLAN_AUTHENTICATION_FAILED);
        }
        tripPlanDao.delete(tripPlanId);
    }

}
