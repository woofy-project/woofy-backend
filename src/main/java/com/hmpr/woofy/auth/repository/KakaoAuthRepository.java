package com.hmpr.woofy.auth.repository;

import com.hmpr.woofy.auth.entity.KakaoAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KakaoAuthRepository extends JpaRepository <KakaoAuth, Long> {

    Optional<KakaoAuth> findByKakaoId(Long kakaoId);

}
