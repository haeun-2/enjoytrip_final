package com.ssafy.trip.domain.article.qna;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QnaAnswerDao {

    int insert(QnaAnswer answer);

    int update(QnaAnswer answer);

    int delete(Long id);

    QnaAnswer selectById(Long id);

}
