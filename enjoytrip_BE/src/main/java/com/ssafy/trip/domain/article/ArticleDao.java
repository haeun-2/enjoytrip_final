package com.ssafy.trip.domain.article;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.trip.domain.article.enums.ArticleType;
import com.ssafy.trip.domain.article.qna.ArticleAnswer;
import com.ssafy.trip.domain.article.qna.QnaArticleSearchCondition;

@Mapper
public interface ArticleDao {
		
	List<Article> selectAll(ArticleSearchCondition condition);
	
	Long countAll(ArticleSearchCondition condition);
	
	Article select(Long id, Boolean isDeleted, Long loginMemberId);
	
	Integer insert(Article article);
	
	Integer update(Article article);
	
	Integer updateViewCnt(Article article);

	Integer updateLikeCnt(Article article);
	
	Integer delete(Long id);
	
	Article selectPrevious(Long id, ArticleType articleType);
	
	Article selectNext(Long id, ArticleType articleType);
	
	List<ArticleAnswer> selectQnaList(QnaArticleSearchCondition condition);
	
	Long countQna(QnaArticleSearchCondition condition);

}
