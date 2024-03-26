package com.hmpr.woofy.user.controller;

import com.hmpr.woofy.common.dto.CommonApiResponse;
import com.hmpr.woofy.user.dto.RegisterUserRequest;
import com.hmpr.woofy.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<CommonApiResponse> registerUser(@RequestParam RegisterUserRequest registerUserRequest) {
            userService.registerUser(registerUserRequest);
            return ResponseEntity.ok(CommonApiResponse.createSuccessWithNoContent());
    }
}