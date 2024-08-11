package com.hmpr.woofy.auth.adapter.impl;

import com.hmpr.woofy.auth.adapter.KakaoAuthAdapter;
import com.hmpr.woofy.auth.dto.KakaoIdResponse;
import com.hmpr.woofy.auth.dto.KakaoTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 카카오 인증 서버로 토큰과 사용자 정보를 가져오는 아답터
 */
@Component
public class KakaoAuthAdapterImpl implements KakaoAuthAdapter {


    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.client-secret}")
    private String kakaoClientSecret;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    /**
     * 카카오 서버에서 받은 code를 이용해서 토큰들을 가져옴
     *
     * @param code 카카오서버에서 받은 코드
     * @return 액세스, 리프래쉬 토큰
     */
    @Override
    public KakaoTokenResponse getToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(tokenUrl)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", kakaoClientId)
                .queryParam("client_secret", kakaoClientSecret)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("code", code);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<KakaoTokenResponse> responseEntity = new RestTemplate().exchange(
                builder.toUriString(),
                HttpMethod.POST,
                entity,
                KakaoTokenResponse.class
        );

        return responseEntity.getBody();
    }

    /**
     * 카카오 서버로 부터 받은 액세스토큰으로 사용자의 아이디를 가져옴
     *
     * @param accessToken 개인용 액세스 토큰
     * @return 카카오아이디 값
     */
    @Override
    public KakaoIdResponse getKakaoUserId(String accessToken) {
        String profileUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<KakaoIdResponse> responseEntity = new RestTemplate().exchange(
                profileUrl,
                HttpMethod.GET,
                entity,
                KakaoIdResponse.class
        );
        return responseEntity.getBody();
    }
}
