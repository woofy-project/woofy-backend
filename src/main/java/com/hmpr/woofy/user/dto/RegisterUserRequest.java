package com.hmpr.woofy.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserRequest {

    Long userId;

    String nickname;

    String woofyName;

    Long woofyAge;

}