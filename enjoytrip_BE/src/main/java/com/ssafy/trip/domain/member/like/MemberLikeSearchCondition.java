package com.ssafy.trip.domain.member.like;

import com.ssafy.trip.common.controller.PageRequest;

public class MemberLikeSearchCondition {
	
	private Integer offset;
	private Integer size;
	private Long memberId;
	
	public MemberLikeSearchCondition(PageRequest request, Long memberId) {
		this.offset = (request.getPage() - 1) * request.getSize();
		this.size = request.getSize();
		this.memberId = memberId;
	}
	
}
