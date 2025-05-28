package com.ssafy.trip.common.controller;

import java.util.List;
import java.util.function.Function;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Page<T> {

	private int navSize = 5;
	private long totalCount;
	
	private int currentPage;
	private int totalPage;
	private int startPage;
	private int endPage;
	
    private List<T> list;
	
    private Boolean hasNext;
    private Boolean hasPre;
    
    public Page(int currentPage, long totalCount, int size, List<T> list) {
    	this.totalCount = totalCount;
    	
    	this.currentPage = currentPage;
    	this.totalPage = (int) Math.ceil((double) totalCount / size);
        this.startPage = (currentPage - 1) / navSize * navSize + 1;
        this.endPage = Math.min(startPage + navSize - 1, totalPage);
    	
    	this.list = list;
    	
    	this.hasPre = currentPage > 1;
        this.hasNext = currentPage < totalPage;
    }

	public static <U,T> Page<T> from(int currentPage, long totalCount, int size, List<U> data, Function<U,T> converter) {
		return new Page<>(
				currentPage,
				totalCount,
				size,
				data.stream().map(converter).toList()
		);
	}

	public static <U,T> Page<T> from(PageRequest page, long totalCount, List<U> data, Function<U,T> converter) {
		return new Page<>(
				page.getPage(),
				totalCount,
				page.getSize(),
				data.stream().map(converter).toList()
		);
	}
	
}
