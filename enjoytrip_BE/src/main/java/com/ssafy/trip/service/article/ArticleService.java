package com.ssafy.trip.service.article;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.trip.common.controller.Page;
import com.ssafy.trip.common.exception.BaseException;
import com.ssafy.trip.common.exception.ErrorCode;
import com.ssafy.trip.controller.article.ArticleRequest;
import com.ssafy.trip.controller.article.ArticleResponse;
import com.ssafy.trip.domain.article.Article;
import com.ssafy.trip.domain.article.ArticleDao;
import com.ssafy.trip.domain.article.ArticleSearchCondition;
import com.ssafy.trip.domain.article.enums.ArticleType;
import com.ssafy.trip.domain.article.qna.ArticleAnswer;
import com.ssafy.trip.domain.article.qna.QnaAnswer;
import com.ssafy.trip.domain.article.qna.QnaAnswerDao;
import com.ssafy.trip.domain.article.qna.QnaArticleSearchCondition;
import com.ssafy.trip.domain.auth.enums.Role;
import com.ssafy.trip.domain.member.Member;
import com.ssafy.trip.domain.member.MemberDao;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleService {

	private final ArticleDao articleDao;
	private final MemberDao memberDao;
	private final QnaAnswerDao answerDao;
	
	public Page<ArticleResponse.Info> selectAll(ArticleRequest.SearchCondition request, String email) {
		ArticleSearchCondition condition = request.toEntity();
		if(email != null) condition.setMemberId(findMember(email).getId());	
		List<Article> articles = articleDao.selectAll(condition);
		Long totalCount = articleDao.countAll(condition);
		return Page.from(condition.getPage(), totalCount, condition.getSize(), articles, ArticleResponse.Info::from);
	}
	
	public Page<ArticleResponse.QnaInfo> selectAllQna(ArticleRequest.QnaSearchCondition request, String email) {
		QnaArticleSearchCondition condition = request.toEntity();
		condition.setMemberId(findMember(email).getId());
		
		List<ArticleAnswer> articles = articleDao.selectQnaList(condition);
		Long totalCount = articleDao.countQna(condition);
		return Page.from(condition.getPage(), totalCount, condition.getSize(), articles, ArticleResponse.QnaInfo::from);
	}

	@Transactional
	public ArticleResponse.DetailInfo select(Long id, Long memberId) {
		Article article = findArticle(id, memberId);
		article.updateViewCount();
		articleDao.updateViewCnt(article);
		Article prev = articleDao.selectPrevious(id, article.getArticleType());
		Article next = articleDao.selectNext(id, article.getArticleType());
		return new ArticleResponse.DetailInfo(article, prev, next);
	}
	
	@Transactional
	public Long insert(ArticleRequest.Create request, String email) {
		Article article = request.toEntity();
		article.setMember(findMember(email));
		articleDao.insert(article);
		
		if(article.getArticleType() == ArticleType.QNA) {
			answerDao.insert(new QnaAnswer(article.getId()));
		}
		
		return article.getId();
	}
	
	public void update(ArticleRequest.Update request, Long id, String email) {
		Article article = findArticle(id, null);
		isWriter(article, findMember(email));
		article.update(request.getTitle(), request.getContent());
		articleDao.update(article);
	}
	
	public void delete(Long id, String email) {
		Article article = findArticle(id, null);
		isWriter(article, findMember(email));
		articleDao.delete(article.getId());
	}
	
		
	// 게시글, 멤버 확인
	private void isWriter(Article article, Member member) {
		if (article.getMember().getId() != member.getId()) {
			throw new BaseException(ErrorCode.ARTICLE_AUTHENTICATION_FAILED);
		}
	}
	
	private Article findArticle(Long id, Long memberId) {
		Article article = articleDao.select(id, false, memberId);
		if(article == null) {
			throw new BaseException(ErrorCode.ARTICLE_NOT_FOUND);
		}
		return article;
	}
	
	private Member findMember(String email) {
		if(email == null) return null;
		
		Member member = memberDao.selectByEmail(email);
		if(member == null) {
			throw new BaseException(ErrorCode.ARTICLE_AUTHENTICATION_FAILED);
		}
		return member;
	}
}
