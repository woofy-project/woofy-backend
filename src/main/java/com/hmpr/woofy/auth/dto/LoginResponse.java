package com.hmpr.woofy.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LoginResponse {

    private Long userId;

    private int status;

    private String message;

    private boolean existingMember;

}
