package com.hmpr.woofy.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

    private int status;

    private String message;

    private boolean existingMember;

}
