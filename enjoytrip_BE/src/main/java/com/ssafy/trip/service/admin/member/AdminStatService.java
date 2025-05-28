package com.ssafy.trip.service.admin.member;

import com.ssafy.trip.controller.admin.member.AdminStatResponse;
import com.ssafy.trip.domain.admin.AdminStatDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminStatService {
    private final AdminStatDao adminStatDao;

    public AdminStatResponse.Count getCount() {
        return adminStatDao.getStatCount();
    }

    public AdminStatResponse.MonthlyStat getMonthlyStat() {
        List<AdminStatResponse.MonthlyRow> rows = adminStatDao.getMonthlyRows();
        AdminStatResponse.MonthlyStat stat = new AdminStatResponse.MonthlyStat();
        stat.setMonthLabel(rows.stream().map(AdminStatResponse.MonthlyRow::getMonth).collect(Collectors.toList()));
        stat.setArticles(rows.stream().map(AdminStatResponse.MonthlyRow::getArticleCount).collect(Collectors.toList()));
        stat.setMembers(rows.stream().map(AdminStatResponse.MonthlyRow::getMemberCount).collect(Collectors.toList()));
        stat.setOpenReports(rows.stream().map(AdminStatResponse.MonthlyRow::getReportCount).collect(Collectors.toList()));
        stat.setOpenQnAs(rows.stream().map(AdminStatResponse.MonthlyRow::getQnaCount).collect(Collectors.toList()));
        stat.setPlans(rows.stream().map(AdminStatResponse.MonthlyRow::getPlanCount).collect(Collectors.toList()));
        return stat;
    }

    public AdminStatResponse.MemberCount getMemberCount() {
        return adminStatDao.getMemberCount();
    }

    public AdminStatResponse.CategoryCount getCategoryCount() {
        return adminStatDao.getCategoryCount();
    }
}
