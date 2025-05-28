package com.ssafy.trip.service.attraction;

import java.util.List;

import com.ssafy.trip.common.controller.Page;
import com.ssafy.trip.common.controller.PageRequest;
import com.ssafy.trip.common.wordsearch.SearchEngine;
import com.ssafy.trip.security.auth.util.AuthenticationUserUtil;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.ssafy.trip.common.exception.BaseException;
import com.ssafy.trip.common.exception.ErrorCode;
import com.ssafy.trip.controller.attraction.AttractionRequest;
import com.ssafy.trip.controller.attraction.AttractionResponse;
import com.ssafy.trip.domain.attraction.Attraction;
import com.ssafy.trip.domain.attraction.AttractionDao;
import com.ssafy.trip.domain.attraction.AttractionSearchCondition;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttractionService {

	private final AttractionDao aDao;

	public Page<AttractionResponse.Info> getListWithPaging(AttractionRequest.PagingCondition request, PageRequest pageRequest) {
		AttractionSearchCondition condition = request.toEntity(pageRequest);
		List<Attraction> attractions = aDao.getListWithPaging(condition);
		int totalCount = aDao.getTotalCount(condition);
		return Page.from(pageRequest, totalCount, attractions, AttractionResponse.Info::from);
	}
	
	public AttractionResponse.SearchInfo search(AttractionRequest.SearchCondition request) {
		AttractionSearchCondition condition = request.toEntity();
		List<Attraction> attractions = aDao.search(condition);
		return new AttractionResponse.SearchInfo(request, attractions);
	}
	
	public Attraction select(Long attractionId, Authentication authentication) {
		Long loginUserId = (authentication != null) ? AuthenticationUserUtil.getUserId(authentication) : null;
		AttractionSearchCondition condition = AttractionSearchCondition.builder()
				.attractionId(attractionId)
				.loginMemberId(loginUserId)
				.build();

		Attraction attraction = aDao.select(condition);
		if(attraction == null) {
			throw new BaseException(ErrorCode.ATTRACTION_NOT_FOUND);
		}
		return attraction;
	}

	public List<AttractionResponse.Name> searchKeyword(String prefix) {
		return SearchEngine.search(prefix).stream()
				.map(AttractionResponse.Name::from)
				.toList();
	}

	public List<AttractionResponse.Info> getListNearby(AttractionRequest.Nearby request) {
		BoxArea boxArea = calculateBoxRange(request.getLat(), request.getLon(), request.getDistance());
		AttractionSearchCondition condition = AttractionSearchCondition.from(boxArea, request.getCategoryId());
		List<Attraction> attractions = aDao.getListNearby(condition);
		return attractions.stream().map(AttractionResponse.Info::from).toList();
	}

	private BoxArea calculateBoxRange(double lat, double lon, int distance) {
		double earthRadius = 6371000; // m
		double latDelta = Math.toDegrees(distance / earthRadius);
		double lonDelta = Math.toDegrees(distance / (earthRadius * Math.cos(Math.toRadians(lat))));

		return new BoxArea(
				lat - latDelta,
				lat + latDelta,
				lon - lonDelta,
				lon + lonDelta
		);
	}

	public record BoxArea(double minLat, double maxLat, double minLon, double maxLon) {}
}
