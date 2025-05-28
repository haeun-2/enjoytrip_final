package com.ssafy.trip.domain.admin;

import com.ssafy.trip.controller.admin.member.AdminStatResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminStatDao {

    AdminStatResponse.Count getStatCount();

    AdminStatResponse.MemberCount getMemberCount();

    List<AdminStatResponse.MonthlyRow> getMonthlyRows();

    AdminStatResponse.CategoryCount getCategoryCount();

}
