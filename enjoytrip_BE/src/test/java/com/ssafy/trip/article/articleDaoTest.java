package com.ssafy.trip.article;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.trip.domain.article.Article;
import com.ssafy.trip.domain.article.ArticleDao;
import com.ssafy.trip.domain.article.ArticleSearchCondition;
import com.ssafy.trip.domain.article.enums.ArticleSortType;
import com.ssafy.trip.domain.article.enums.ArticleType;
import com.ssafy.trip.domain.attraction.region.Sido;
import com.ssafy.trip.domain.attraction.region.SidoDao;
import com.ssafy.trip.domain.member.Member;
import com.ssafy.trip.domain.member.MemberDao;

@MybatisTest
@Transactional
public class articleDaoTest {
	
	@Autowired ArticleDao aDao;
	@Autowired MemberDao mDao;
	
	@BeforeEach
	public void setup() {
		Member testMember = new Member("test@test.com", "test", "test", null, null, null, null);
		mDao.insert(testMember);
		aDao.insert(new Article("test1", "test content...",  testMember, ArticleType.FREE));
		aDao.insert(new Article("test2", "test content...",  testMember, ArticleType.FREE));
		aDao.insert(new Article("test3", "test content...",  testMember, ArticleType.FREE));
		System.out.println("ok");
	}
	
	@Test
	public void selectAllTest() {
		Member testMember = mDao.selectByEmail("test@test.com");
		
		List<Article> list = aDao.selectAll(new ArticleSearchCondition());
		Assertions.assertEquals(3, list.size());
		Assertions.assertNull(list.get(0).getContent());
				
		list = aDao.selectAll(ArticleSearchCondition.builder().page(2).size(2).build());
		Assertions.assertEquals(1, list.size());
	
		list = aDao.selectAll(ArticleSearchCondition.builder().page(1).size(10).memberId(testMember.getId()).build());
		Assertions.assertEquals(3, list.size());
		
		list = aDao.selectAll(ArticleSearchCondition.builder().page(1).size(10).memberId(0L).build());
		Assertions.assertEquals(0, list.size());
		
		list = aDao.selectAll(ArticleSearchCondition.builder().page(1).size(10).title("1").build());
		Assertions.assertEquals("test1", list.get(0).getTitle());
		
		list = aDao.selectAll(ArticleSearchCondition.builder().page(1).size(10).build());
		Assertions.assertEquals(2, list.size());
	}
	
	@Test
	public void countTest() {
		Long total = aDao.countAll(new ArticleSearchCondition());
		Assertions.assertEquals(3L, total);
	}
	
	@Test
	public void select() {
		Article article =  aDao.selectAll(new ArticleSearchCondition()).get(0);
		Article findArticle = aDao.select(article.getId(), false, null);
		Assertions.assertEquals("test content...", findArticle.getContent());
		Assertions.assertNull(findArticle.getDeletedAt());
		Assertions.assertNotNull(findArticle.getMember());
	}
	
	@Test
	public void insert() {
		Member testMember = mDao.selectByEmail("test@test.com");
		
		Integer result = aDao.insert(new Article("test4", "test content...", testMember, ArticleType.FREE));
		
		Assertions.assertEquals(1, result);
		
		List<Article> list = aDao.selectAll(new ArticleSearchCondition());
		Assertions.assertEquals(4, list.size());
	}
	
	@Test
	public void update() {
		Article article = aDao.selectAll(new ArticleSearchCondition()).get(0);
		article.update("test 수정", null);
		
		aDao.update(article);
		
		Article updatedArticle =  aDao.select(article.getId(), false, null);
		Assertions.assertEquals("test 수정", updatedArticle.getTitle());
		Assertions.assertNotNull(updatedArticle.getModifiedAt());
		Assertions.assertEquals(article.getViewCount(), updatedArticle.getViewCount());	
	}
	
	@Test
	public void updateViewCnt() {
		Article article = aDao.selectAll(new ArticleSearchCondition()).get(0);
		int originalViewCnt = article.getViewCount();
		article.getViewCount();
		aDao.updateViewCnt(article);
		
		Article updatedArticle =  aDao.select(article.getId(), false, null);
		System.out.println(updatedArticle);
		Assertions.assertEquals(article.getTitle(), updatedArticle.getTitle());
		Assertions.assertNull(updatedArticle.getModifiedAt());
		Assertions.assertEquals(originalViewCnt+1, updatedArticle.getViewCount());
		
		List<Article> list = aDao.selectAll(ArticleSearchCondition.builder().page(1).size(10).sort(ArticleSortType.VIEW_COUNT).build());
		Assertions.assertEquals(updatedArticle.getId(), list.get(0).getId());
	}
	
	@Test
	public void delete() {
		Article article = aDao.selectAll(new ArticleSearchCondition()).get(0);
		aDao.delete(article.getId());
		
		Article deletedArticle =  aDao.select(article.getId(), false, null);
		Assertions.assertNull(deletedArticle);
		
		List<Article> list = aDao.selectAll(new ArticleSearchCondition());
		Assertions.assertEquals(2, list.size());
	}
	
}
