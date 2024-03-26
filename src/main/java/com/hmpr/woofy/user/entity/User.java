package com.hmpr.woofy.user.entity;

import com.hmpr.woofy.auth.entity.KakaoAuth;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long userId;

    private String email;

    private String nickname;

    @Column(name = "woofy_name")
    private String woofyName;

    @Column(name = "woofy_age")
    private Long woofyAge;

    @Column(name = "self_intro", columnDefinition = "TEXT")
    private String selfIntro;

}
