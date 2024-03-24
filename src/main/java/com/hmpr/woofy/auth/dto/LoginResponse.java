package com.hmpr.woofy.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

    private Long userId;

    private int status;

    private String message;

    private boolean existingMember;

}
