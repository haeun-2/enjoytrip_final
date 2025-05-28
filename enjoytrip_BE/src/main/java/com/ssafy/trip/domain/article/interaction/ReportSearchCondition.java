package com.ssafy.trip.domain.article.interaction;

import com.ssafy.trip.domain.article.enums.ReportStatus;

import lombok.Getter;

@Getter
public class ReportSearchCondition {
	
	private Integer page;
	private Integer size;
	private ReportStatus status;

	public ReportSearchCondition() {
		this(null, null, null);
	}
	
	public ReportSearchCondition(Integer page, Integer size, ReportStatus status) {
		this.page = (page == null || page < 1) ? 1 : page;
        this.size = (size == null || size < 1) ? 10 : size;
        this.status = status;
	}
	
	public int getOffset() {
        return (page - 1) * size;
    }
    

}
