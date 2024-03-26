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
     * @return 기존회원인 경우 로그인 폼 / 아닌경우
     */
    @Override
    public LoginResponse kakaoLogin(String accessToken) {
        KakaoIdResponse kakaoIdResponse = kakaoAuthAdapter.getKakaoUserId(accessToken);
        Optional<KakaoAuth> kakaoAuthOptional = kakaoAuthRepository.findByKakaoId(kakaoIdResponse.getKakaoId());

        if (kakaoAuthOptional.isPresent()) {
            return successLogin(kakaoAuthOptional.get());
        } else {
            return registerNewUser(kakaoIdResponse);
        }
    }

    /**
     * 카카오 로그인 성공 후 유저 아이디 반환
     *
     * @param kakaoAuth 카카오 아이디를 가져오고 유저아이디와 매칭
     * @return 매칭된 유저아이디
     */
    private LoginResponse successLogin(KakaoAuth kakaoAuth) {
        User loginUser = kakaoAuth.getUser();
        Long userId = loginUser.getUserId();
        return buildLoginResponse(userId, "로그인 성공", true);
    }

    /**
     * 카카오 로그인 이후 새로운 회원일 경우 회원가입 요청
     *
     * @param kakaoIdResponse 카카오서버로부터 받은 카카오아이디
     * @return 새로운 유저아이디만 저장 후 회원가입 요청 메세지
     */
    private LoginResponse registerNewUser(KakaoIdResponse kakaoIdResponse) {
        User newUser = new User();
        User savedUser = userRepository.save(newUser);

        KakaoAuth newKakaoAuth = new KakaoAuth();
        newKakaoAuth.setKakaoId(kakaoIdResponse.getKakaoId());
        newKakaoAuth.setUser(savedUser);
        kakaoAuthRepository.save(newKakaoAuth);

        return buildLoginResponse(savedUser.getUserId(), "회원가입 필요", false);
    }

    /**
     * 응답 빌더
     * @param userId 유저아이디
     * @param message 메세지
     * @param existingMember 회원가입필요 여부
     * @return 빌더된 응답
     */
    private LoginResponse buildLoginResponse(Long userId, String message, boolean existingMember) {
        return LoginResponse.builder()
                .userId(userId)
                .status(200)
                .message(message)
                .existingMember(existingMember)
                .build();
    }
}