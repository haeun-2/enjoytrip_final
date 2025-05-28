package com.ssafy.trip.memberplace;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.trip.domain.attraction.CategoryDao;
import com.ssafy.trip.domain.attraction.region.GugunDao;
import com.ssafy.trip.domain.attraction.region.SidoDao;
import com.ssafy.trip.domain.auth.AuthDao;
import com.ssafy.trip.domain.member.Member;
import com.ssafy.trip.domain.member.MemberDao;
import com.ssafy.trip.domain.memberplace.MemberPlace;
import com.ssafy.trip.domain.memberplace.MemberPlaceDao;
import com.ssafy.trip.domain.memberplace.MemberPlaceSearchCondition;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // H2 끄고 MySQL 사용
@Transactional
public class MemberPlaceDaoTest {
	
	@Autowired MemberPlaceDao mpDao;
	@Autowired MemberDao mDao;
	@Autowired SidoDao sidoDao;
	@Autowired GugunDao gugunDao;
	@Autowired CategoryDao categoryDao;
	
	Member mockMember;
	MemberPlace mockMp1, mockMp2, mockMp3;
	
	@BeforeEach
	public void beforeEach() {
		mDao.insert(Member.builder().name("test").email("test@test.com").build());
		mockMember = mDao.selectByEmail("test@test.com");
		
		mockMp1 = MemberPlace.builder().name("test1").member(mockMember).sido(sidoDao.select(18L)).gugun(gugunDao.select(235L)).category(categoryDao.select(12L)).build();
		mockMp2 = MemberPlace.builder().name("test2").member(mockMember).sido(sidoDao.select(18L)).gugun(gugunDao.select(235L)).category(categoryDao.select(12L)).build();
		mockMp3 = MemberPlace.builder().name("test3").member(mockMember).sido(sidoDao.select(18L)).gugun(gugunDao.select(235L)).category(categoryDao.select(12L)).build();

		mpDao.insert(mockMp1);
		mpDao.insert(mockMp2);
		mpDao.insert(mockMp3);
		System.out.println(mockMp1);
	}
	
	@Test
	public void selectAll() {
		List<MemberPlace> list = mpDao.selectAll(new MemberPlaceSearchCondition());
		Assertions.assertEquals(3, list.size());
		
		list = mpDao.selectAll(MemberPlaceSearchCondition.builder().page(1).size(2).build());
		Assertions.assertEquals(2, list.size());	
	}
	
	@Test
	public void selectAllByMember() {
		List<MemberPlace> list = mpDao.selectAll(MemberPlaceSearchCondition.builder().memberId(mockMember.getId()).build());
		Assertions.assertEquals(3, list.size());
		
		list = mpDao.selectAll(MemberPlaceSearchCondition.builder().memberId(0L).build());
		Assertions.assertEquals(0, list.size());
	}
	
	@Test
	public void select() {
		MemberPlace mp = mpDao.select(mockMp1.getId());
		Assertions.assertEquals("test1", mp.getName());
	}
	
	@Test
	public void insertTest() {
		MemberPlace mp = MemberPlace.builder().name("test").member(mockMember).sido(sidoDao.select(18L)).gugun(gugunDao.select(235L)).category(categoryDao.select(12L)).build();

		Integer result = mpDao.insert(mp);
		Assertions.assertEquals(1, result);
		
		List<MemberPlace> list = mpDao.selectAll(new MemberPlaceSearchCondition());
		Assertions.assertEquals(4, list.size());
	}
	
	@Test
	public void update() {
		mockMp1.update("test 수정", null, null, null, null, null, null, null, null, null);	
		Integer result = mpDao.update(mockMp1);
		Assertions.assertEquals(1, result);
		
		MemberPlace mp = mpDao.select(mockMp1.getId());
		Assertions.assertEquals("test 수정", mp.getName());
		Assertions.assertEquals(18L, mp.getSido().getId());
	}
	
	@Test
	public void delete() {
		Integer result = mpDao.delete(mockMp1.getId());
		Assertions.assertEquals(1, result);
		
		MemberPlace mp = mpDao.select(mockMp1.getId());
		Assertions.assertNull(mp);
	}
	
}
