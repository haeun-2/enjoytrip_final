package com.ssafy.trip.service.article;

import com.ssafy.trip.domain.article.Article;
import com.ssafy.trip.domain.article.ArticleDao;
import com.ssafy.trip.domain.article.interaction.ArticleLike;
import com.ssafy.trip.domain.article.interaction.ArticleInteractionDao;
import com.ssafy.trip.domain.article.interaction.ArticleReport;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleInteractionService {
    private final ArticleInteractionDao articleInteractionDao;
    private final ArticleDao articleDao;

    public void like(Long articleId, Long memberId) {
        ArticleLike articleLike = new ArticleLike(articleId, memberId);
        int result = articleInteractionDao.exists(articleLike);
        if (result > 0) {
            articleInteractionDao.reInsert(articleLike);
        } else {
            articleInteractionDao.insert(articleLike);
        }
        Article article = articleDao.select(articleId, false, null);
        article.addLikeCount();
        articleDao.updateLikeCnt(article);
    }

    public void unlike(Long articleId, Long memberId) {
        ArticleLike articleLike = new ArticleLike(articleId, memberId);
        articleInteractionDao.delete(articleLike);
        Article article = articleDao.select(articleId, false, null);
        article.subLikeCount();
        articleDao.updateLikeCnt(article);
    }

    public void report(Long articleId, Long memberId, String reason) {
        ArticleReport articleReport = new ArticleReport(articleId, memberId, reason);
        articleInteractionDao.insertReport(articleReport);
    }
    
}