package com.ssafy.trip.controller.auth;

import com.ssafy.trip.domain.member.Member;
import com.ssafy.trip.security.auth.CustomUserDetails;
import com.ssafy.trip.service.EmailAuthService;
import com.ssafy.trip.service.OAuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.trip.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "사용자 인증")
public class AuthController {
    private final AuthService authService;
    private final OAuthService oAuthService;
    private final EmailAuthService emailAuthService;

    @Operation(summary = "회원가입")
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse.SignIn> signin(@RequestBody AuthRequest.SignIn request) {
        AuthResponse.SignIn response = authService.singIn(request);
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "이메일 인증 메일 전송")
    @PostMapping("/email/send")
    public ResponseEntity<String> sendEmail(@RequestParam("email") String email) {
        emailAuthService.checkEmailDuplicateAndSendVerificationEmail(email);
        return ResponseEntity.ok("이메일 인증 메일 전송 완료");
    }
    @Operation(summary = "이메일 인증")
    @GetMapping("/email/verify")
    public ResponseEntity<AuthResponse.Message> verifyEmail(@RequestParam("email") String email, @RequestParam("code") String code) {
        boolean verified = emailAuthService.verifyEmail(email, code);
        var response = AuthResponse.Message.from(verified, verified ? "이메일 인증 완료" : "이메일 인증 실패");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse.Login> login(@RequestBody AuthRequest.Login request, HttpServletRequest httpRequest) {
        // 서비스에서 로그인 인증 처리
        AuthResponse.Login.Data data = authService.login(request);

        // 인증된 사용자 정보를 세션에 저장
        HttpSession session = httpRequest.getSession();
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );
        return ResponseEntity.ok(AuthResponse.Login.from(data, null, "로그인 성공"));
    }
    
    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession();
        session.invalidate();
        return ResponseEntity.ok("로그아웃 완료");
    }
    

    @GetMapping("/kakao/callback")
    public void kakaoOAuth(@RequestParam("code") String code,
                                             HttpServletResponse response,
                                             HttpSession session) throws IOException  {
        oAuthService.kakaoOAuth(code);
        // 인증된 사용자 정보를 세션에 저장
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );
        sendUser(response);
    }

    @GetMapping("/naver/callback")
    public void naverOAuth(@RequestParam("code") String code,
                                             @RequestParam("state") String state,
                                             HttpServletResponse response,
                                             HttpSession session) throws IOException {
        oAuthService.naverOAuth(code, state);
        // 인증된 사용자 정보를 세션에 저장
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );
        sendUser(response);
    }

    private void sendUser(HttpServletResponse response) throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = userDetails.getMember();
        String redirectUrl = String.format(
                "http://localhost:5173/login" +
                        "?id=%d&email=%s&name=%s&profileImage=%s&role=%s",
                member.getId(),
                URLEncoder.encode(member.getEmail(), StandardCharsets.UTF_8),
                URLEncoder.encode(member.getName(), StandardCharsets.UTF_8),
                URLEncoder.encode(member.getProfileImage(), StandardCharsets.UTF_8),
                URLEncoder.encode(member.getRole().name(), StandardCharsets.UTF_8)
        );
        response.sendRedirect(UriComponentsBuilder.fromUriString(redirectUrl).toUriString());
    }
}
