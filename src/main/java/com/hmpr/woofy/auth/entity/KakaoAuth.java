package com.hmpr.woofy.auth.entity;

import com.hmpr.woofy.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "kakao_auth")
public class KakaoAuth {

    @Id
    @Column(name = "kakao_id")
    private Long kakaoId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
