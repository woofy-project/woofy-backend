package com.hmpr.woofy.user.service;

import com.hmpr.woofy.user.dto.RegisterUserRequest;
import com.hmpr.woofy.user.entity.User;

public interface UserService {

    void registerUser(RegisterUserRequest registerUserRequest);

    User getUserById(Long userId);
}
