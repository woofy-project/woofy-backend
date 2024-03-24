package com.hmpr.woofy.auth.service.impl;

import com.hmpr.woofy.auth.adapter.KakaoAuthAdapter;
import com.hmpr.woofy.auth.dto.KakaoIdResponse;
import com.hmpr.woofy.auth.dto.KakaoTokenResponse;
import com.hmpr.woofy.auth.dto.LoginResponse;
import com.hmpr.woofy.auth.entity.KakaoAuth;
import com.hmpr.woofy.auth.repository.KakaoAuthRepository;
import com.hmpr.woofy.auth.service.KakaoAuthService;
import com.hmpr.woofy.user.entity.User;
import com.hmpr.woofy.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 카카오 로그인을 위한 인증 서비스
 */
@Service
public class KakaoAuthServiceImpl implements KakaoAuthService {

    private final KakaoAuthAdapter kakaoAuthAdapter;
    private final KakaoAuthRepository kakaoAuthRepository;
    private final UserRepository userRepository;

    public KakaoAuthServiceImpl(KakaoAuthAdapter kakaoAuthAdapter, KakaoAuthRepository kakaoAuthRepository, UserRepository userRepository) {
        this.kakaoAuthAdapter = kakaoAuthAdapter;
        this.kakaoAuthRepository = kakaoAuthRepository;
        this.userRepository = userRepository;
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
     * 기존 회원인 경우와 회원가입이 필요한 경우를 분류
     *
     * @param accessToken 카카오서버로부터 받은 토큰
     * @return 기존 회원인 경우 existingMember = true
     */
    @Override
    public LoginResponse kakaoLogin(String accessToken) {
        KakaoIdResponse kakaoIdResponse = kakaoAuthAdapter.getKakaoUserId(accessToken);

        Optional<KakaoAuth> kakaoAuthOptional = kakaoAuthRepository.findByKakaoId(kakaoIdResponse.getKakaoId());
        LoginResponse loginResponse = new LoginResponse();

        if (kakaoAuthOptional.isPresent()) {
            KakaoAuth kakaoAuth = kakaoAuthOptional.get();
            User user = kakaoAuth.getUser();
            Long userId = user.getUserId();
            loginResponse.setUserId(userId);
            loginResponse.setStatus(200);
            loginResponse.setMessage("로그인 성공");
            loginResponse.setExistingMember(true);
        } else {
            User newUser = new User();
            User savedUser = userRepository.save(newUser);
            System.out.println(newUser.getUserId());
            KakaoAuth newKakaoAuth = new KakaoAuth();
            newKakaoAuth.setKakaoId(kakaoIdResponse.getKakaoId());
            newKakaoAuth.setUser(savedUser);
            System.out.println(kakaoIdResponse.getKakaoId());
            kakaoAuthRepository.save(newKakaoAuth);
            Long userId = savedUser.getUserId();
            loginResponse.setUserId(userId);
            loginResponse.setStatus(200);
            loginResponse.setMessage("회원가입 필요");
            loginResponse.setExistingMember(false);
        }
        return loginResponse;
    }
}