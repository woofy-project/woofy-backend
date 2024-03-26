package com.hmpr.woofy.auth.service;

import com.hmpr.woofy.auth.dto.LoginResponse;

public interface KakaoAuthService {

    String getAccessToken(String code);

    LoginResponse kakaoLogin(String accessToken);
}