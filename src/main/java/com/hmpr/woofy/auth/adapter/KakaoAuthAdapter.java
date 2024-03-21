package com.hmpr.woofy.auth.adapter;

import com.hmpr.woofy.auth.dto.KakaoIdResponse;
import com.hmpr.woofy.auth.dto.KakaoTokenResponse;

public interface KakaoAuthAdapter {

    KakaoTokenResponse getToken(String code);

    KakaoIdResponse getUserId(String accessToken);

}
