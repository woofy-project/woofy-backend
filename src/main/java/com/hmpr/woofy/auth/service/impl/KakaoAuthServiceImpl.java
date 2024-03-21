package com.hmpr.woofy.auth.service.impl;

import com.hmpr.woofy.auth.adapter.KakaoAuthAdapter;
import com.hmpr.woofy.auth.dto.KakaoIdResponse;
import com.hmpr.woofy.auth.dto.KakaoTokenResponse;
import com.hmpr.woofy.auth.dto.LoginResponse;
import com.hmpr.woofy.auth.entity.KakaoAuth;
import com.hmpr.woofy.auth.repository.KakaoAuthRepository;
import com.hmpr.woofy.auth.service.KakaoAuthService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 카카오 로그인을 위한 인증 서비스
 */
@Service
public class KakaoAuthServiceImpl implements KakaoAuthService {

    private final KakaoAuthAdapter kakaoAuthAdapter;
    private final KakaoAuthRepository kakaoAuthRepository;

    public KakaoAuthServiceImpl(KakaoAuthAdapter kakaoAuthAdapter, KakaoAuthRepository kakaoAuthRepository) {
        this.kakaoAuthAdapter = kakaoAuthAdapter;
        this.kakaoAuthRepository = kakaoAuthRepository;
    }


    /**
     * 개인 액세스 토큰을 카카오서버로부터 받음
     *
     * @param code 개인용 코드
     * @return 액세스 토큰
     */
    @Override
    public String getAccessToken(String code) {
        KakaoTokenResponse tokenResponse = kakaoAuthAdapter.getToken(code);
        return tokenResponse.getAccessToken();
    }

    /**
     * 카카오 로그인 서비스,
     * 기존 회원인 경우와 회원가입이 필용한 경우를 분류
     *
     * @param accessToken 카카오서버로부터 받은 토큰
     * @return 기존 회원인 경우 existingMember = true
     */
    @Override
    public LoginResponse kakaoLogin(String accessToken) {
        KakaoIdResponse kakaoIdResponse = kakaoAuthAdapter.getUserId(accessToken);
        Optional<KakaoAuth> existingUserOptional = kakaoAuthRepository.findByKakaoId(kakaoIdResponse.getId());
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setStatus(200);

        if (existingUserOptional.isPresent()) {
            loginResponse.setMessage("로그인 성공");
            loginResponse.setExistingMember(true);
        } else {
            loginResponse.setMessage("회원가입 필요");
            loginResponse.setExistingMember(false);
        }
        return loginResponse;
    }
}
