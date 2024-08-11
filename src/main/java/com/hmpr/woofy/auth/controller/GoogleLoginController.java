package com.hmpr.woofy.auth.controller;

import com.hmpr.woofy.auth.dto.LoginResponse;
import com.hmpr.woofy.auth.povider.CookieProvider;
import com.hmpr.woofy.auth.service.GoogleAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/auth/google")
public class GoogleLoginController {

    @Value("${google.client-id}")
    private String googleClientId;

    @Value("${google.redirect-uri}")
    private String redirectUri;

    private final GoogleAuthService googleAuthService;
    private final CookieProvider cookieProvider;

    public GoogleLoginController(GoogleAuthService googleAuthService, CookieProvider cookieProvider) {
        this.googleAuthService = googleAuthService;
        this.cookieProvider = cookieProvider;
    }

    @GetMapping("/login")
    public String googleLogin() {
        String authorizationUrl = UriComponentsBuilder.fromHttpUrl("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("client_id", googleClientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "openid profile email")
                .build().toUriString();
        return "redirect:" + authorizationUrl;
    }

    @GetMapping("/auth-code")
    public ResponseEntity<LoginResponse> requestCode(@RequestParam("code") String code, HttpServletResponse response) {
        String accessToken = googleAuthService.getAccessToken(code);
        cookieProvider.addAccessTokenToCookie(response, accessToken);
        LoginResponse loginResponse = googleAuthService.googleLogin(accessToken);
        return ResponseEntity.ok(loginResponse);
    }
}

