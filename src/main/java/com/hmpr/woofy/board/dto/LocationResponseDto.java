package com.hmpr.woofy.board.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LocationResponseDto {

    private String streetAddress;

    private String detail;
}
