package com.ssafy.trip.domain.auth;

import org.apache.ibatis.annotations.*;

@Mapper
public interface OauthProviderDao {

    @Select("""
    SELECT 
        op.id AS op_id,
        op.provider AS op_provider,
        op.`key` AS op_key,
        op.member_id AS op_member_id,
        m.id AS m_id,
        m.email AS m_email,
        m.name AS m_name,
        m.role AS m_role,
        m.created_at AS m_created_at,
        m.profile_image AS m_profile_image
    FROM oauth_provider op
    RIGHT OUTER JOIN member m ON op.member_id = m.id
    WHERE op.`key` = #{key}
""")
    @Results(id = "OAuthProviderWithMember", value = {
            @Result(property = "id", column = "op_id"),
            @Result(property = "provider", column = "op_provider"),
            @Result(property = "key", column = "op_key"),
            @Result(property = "memberId", column = "op_member_id"),
            @Result(property = "member.id", column = "m_id"),
            @Result(property = "member.email", column = "m_email"),
            @Result(property = "member.name", column = "m_name"),
            @Result(property = "member.role", column = "m_role"),
            @Result(property = "member.createdAt", column = "m_created_at"),
            @Result(property = "member.profileImage", column = "m_profile_image")
    })
    OAuthProvider selectByOAuthProviderId(String key);

    @Insert("INSERT INTO oauth_provider (provider, `key`, member_id) " +
            "VALUES (#{provider}, #{key}, #{memberId})")
    int insert(OAuthProvider oauthProvider);
}
