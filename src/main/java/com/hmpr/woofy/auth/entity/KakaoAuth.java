package com.hmpr.woofy.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "kakao_auth")
public class KakaoAuth {

    @Id
    @Column(name = "kakao_id")
    private Long kakaoId;

    @Column(name = "user_id")
    private Long userId;

}
