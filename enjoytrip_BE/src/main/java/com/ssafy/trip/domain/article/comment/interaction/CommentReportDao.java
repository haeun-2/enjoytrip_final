package com.ssafy.trip.domain.article.comment.interaction;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.trip.domain.article.interaction.ReportSearchCondition;

@Mapper
public interface CommentReportDao {
    int insertReport(CommentReport report);
    
    CommentReport selectReport(Long id);
    
    int updateReport(CommentReport report);
    
    List<CommentReport> selectAll(ReportSearchCondition condition); 
    
    Long countAll(ReportSearchCondition condition);

    int block(Long id);

}
