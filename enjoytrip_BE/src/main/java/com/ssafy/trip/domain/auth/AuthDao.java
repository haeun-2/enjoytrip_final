package com.ssafy.trip.domain.auth;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.ssafy.trip.domain.member.Member;

@Mapper
public interface AuthDao {
    @Select("SELECT * FROM member WHERE email = #{email}")
    Member selectByEmail(String email);
}
