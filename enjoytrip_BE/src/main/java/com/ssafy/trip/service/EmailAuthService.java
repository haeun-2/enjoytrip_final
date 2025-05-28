package com.ssafy.trip.service;

import com.ssafy.trip.common.exception.BaseException;
import com.ssafy.trip.common.exception.ErrorCode;
import com.ssafy.trip.domain.member.Member;
import com.ssafy.trip.domain.member.MemberDao;
import com.ssafy.trip.external.mail.MailClient;
import com.ssafy.trip.external.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailAuthService {
    private final MemberDao memberDao;
    private final MailClient mailClient;
    private final RedisUtil redisUtil;


    /**
     * 이메일 중복 체크 후 인증 메일 발송
     */
    public void checkEmailDuplicateAndSendVerificationEmail(String email) {
        Member member = memberDao.selectByEmail(email);
        if (member != null) {
            throw new BaseException(ErrorCode.MEMBER_ALREADY_EXISTS);
        }
        mailClient.sendVerificationMail(email);
    }

    public boolean verifyEmail(String email, String code) {
        String correctCode = (String) redisUtil.getData(email);
        return correctCode != null && correctCode.equals(code);
    }
}
