package com.hmpr.woofy.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LocationRequestDto {

    private String streetAddress;

    private String detail;
}
