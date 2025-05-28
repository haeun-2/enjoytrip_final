package com.ssafy.trip.domain.memberplace;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberPlaceSearchCondition {
	
	private Integer page;
	private Integer size;
	private String sort;
	private String sortDir;
	private Long memberId;
	
	private List<Long> sidoIds;
	private Long categoryId;
	private String keyword;
	
	private static final String DEFAULT_ID_SORT = "id";
	private static final String CREATED_AT_SORT = "created_at";
	private static final String LIKE_COUNT_SORT = "like_count";
	
	private static final String DESC = "desc";
	private static final String ASC = "asc";
	
	private Long loginMemberId;
	
	public MemberPlaceSearchCondition() {
		this(null, null, null, null, null, null, null, null);
	}
	
	@Builder
	public MemberPlaceSearchCondition(Integer page, Integer size, String sort, String sortDir, Long memberId, List<Long> sidoIds, Long categoryId, String keyword) {
		this.page = page != null ? page : 1;
	    this.size = size != null ? size : 10;
	    setSort(sort,sortDir);
	    this.sidoIds = sidoIds;
	    this.categoryId = categoryId;
	    this.memberId = memberId;
	    this.keyword = keyword!= null && !keyword.isBlank() ? keyword.trim() : null;
	}
	 
	public int getOffset() {
        return (page - 1) * size;
    }
    
    public void setMemberId(Long memberId) {
    	this.memberId = memberId;
    }
    
    public void setLoginMemberId(Long loginMemberId) {
    	this.loginMemberId = loginMemberId;
    }
    
    private void setSort(String sort, String sortDir) {
    	this.sort = switch(String.valueOf(sort).toLowerCase()) {
    	case CREATED_AT_SORT, LIKE_COUNT_SORT -> sort;
    	default -> DEFAULT_ID_SORT;
    	};
    	this.sortDir = switch(String.valueOf(sortDir).toLowerCase()) {
    	case DESC, ASC -> sortDir;
    	default -> DESC;
    	};
    }
    
}
