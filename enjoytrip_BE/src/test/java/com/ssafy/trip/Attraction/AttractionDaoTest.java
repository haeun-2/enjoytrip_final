package com.ssafy.trip.Attraction;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ssafy.trip.domain.attraction.Attraction;
import com.ssafy.trip.domain.attraction.AttractionDao;
import com.ssafy.trip.domain.attraction.AttractionSearchCondition;

@SpringBootTest
public class AttractionDaoTest {

	@Autowired AttractionDao aDao;
	
	@Test
	public void search() {
		AttractionSearchCondition condition = new AttractionSearchCondition(1, 1, 12);
		List<Attraction> list = aDao.search(condition);
		Assertions.assertEquals(46, list.size());
	}
	
	@Test
	public void select() {
//		Attraction a = aDao.select(56644L);
//		System.out.println(a);
	}
	
}
