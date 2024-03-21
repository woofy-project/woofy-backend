package com.hmpr.woofy.auth.controller;

import com.hmpr.woofy.auth.dto.LoginResponse;
import com.hmpr.woofy.auth.povider.CookieProvider;
import com.hmpr.woofy.auth.service.KakaoAuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping("/auth/kakao")
public class KakaoLoginController {

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final KakaoAuthService kakaoAuthService;
    private final CookieProvider cookieProvider;

    public KakaoLoginController(KakaoAuthService kakaoAuthService, CookieProvider cookieProvider) {
        this.kakaoAuthService = kakaoAuthService;
        this.cookieProvider = cookieProvider;
    }

    @GetMapping("/login")
    public String kakaoLogin() {
        String authorizationUrl = UriComponentsBuilder.fromHttpUrl("https://kauth.kakao.com/oauth/authorize")
                .queryParam("client_id", kakaoClientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .build().toUriString();
        return "redirect:" + authorizationUrl;
    }

    @GetMapping("/auth-code")
    public ResponseEntity<LoginResponse> requestCode(@RequestParam("code") String code, HttpServletResponse response) {
        String accessToken = kakaoAuthService.getAccessToken(code);
        cookieProvider.addAccessTokenToCookie(response, accessToken);
        LoginResponse loginResponse = kakaoAuthService.kakaoLogin(accessToken);
        return ResponseEntity.ok(loginResponse);
    }
}