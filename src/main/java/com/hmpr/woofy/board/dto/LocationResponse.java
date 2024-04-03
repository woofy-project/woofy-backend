package com.hmpr.woofy.board.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LocationResponse {

    private String streetAddress;

    private String detail;
}
