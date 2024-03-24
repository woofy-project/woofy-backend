package com.hmpr.woofy.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoIdResponse {

    @JsonProperty("id")
    private Long kakaoId;

}
