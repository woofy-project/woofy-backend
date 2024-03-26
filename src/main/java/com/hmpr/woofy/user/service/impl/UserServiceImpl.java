package com.hmpr.woofy.user.service.impl;

import com.hmpr.woofy.user.dto.RegisterUserRequest;
import com.hmpr.woofy.user.entity.User;
import com.hmpr.woofy.user.repository.UserRepository;
import com.hmpr.woofy.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 소셜로그인 후 생성된 userId로 추가 회원가입정보 입력
     *
     * @param registerUserRequest 닉네임,강아지이름/나이 입력
     */
    @Override
    public void registerUser(RegisterUserRequest registerUserRequest) {
        Optional<User> newUser = userRepository.findById(registerUserRequest.getUserId());
        User existingUser = newUser.get();
        existingUser.builder()
                .nickname(registerUserRequest.getNickname())
                .woofyName(registerUserRequest.getWoofyName())
                .woofyAge(registerUserRequest.getWoofyAge())
                .build();
        userRepository.save(existingUser);
    }
}
