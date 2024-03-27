package com.hmpr.woofy.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LocationRequestDto {

    String streetAddress;

    String detail;
}
