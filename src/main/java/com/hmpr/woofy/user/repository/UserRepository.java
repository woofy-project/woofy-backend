package com.hmpr.woofy.user.repository;

import com.hmpr.woofy.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u JOIN KakaoAuth ka ON u.userId = ka.user.userId WHERE ka.kakaoId = :kakaoId")
    Optional<User> findByKakaoId(Long kakaoId);
}
