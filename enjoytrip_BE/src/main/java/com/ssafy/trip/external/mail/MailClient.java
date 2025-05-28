package com.ssafy.trip.external.mail;

import com.ssafy.trip.common.exception.BaseException;
import com.ssafy.trip.common.exception.ErrorCode;
import com.ssafy.trip.external.redis.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.security.SecureRandom;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailClient {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final RedisUtil redisUtil;

    @Value("${spring.mail.username}")
    private String sender;

    private static final String MAIL_TITLE = "[여행 스케치] 이메일 인증";
    private static final String MAIL_CHECK = "mail-check";

    public void sendVerificationMail(String email) {
        String code = generateVerificationCode();
        String mailTemplate = createTemplate(email, code);
        redisUtil.addData(email, code);
        sendMail(email, mailTemplate);
    }

    private void sendMail(String email, String mailTemplate) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "utf-8");

            messageHelper.setFrom(sender);
            messageHelper.setTo(email);
            messageHelper.setSubject(MAIL_TITLE);
            messageHelper.setText(mailTemplate, true);

            mailSender.send(message);
        } catch (MessagingException | MailException e) {
            log.error("메일 전송 실패: {}", e.getMessage());
            throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR, "메일 전송에 실패했습니다.");
        }
    }

    private String createTemplate(String email, String code) {
        Context context = new Context();
        context.setVariable("name", email);
        context.setVariable("code", code);
        return templateEngine.process(MAIL_CHECK, context);
    }

    private String generateVerificationCode() {
        // 인증 코드 생성 (6자리 랜덤 숫자)
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
