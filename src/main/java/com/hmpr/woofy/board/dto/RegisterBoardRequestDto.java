package com.hmpr.woofy.board.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterBoardRequestDto {

    Long userId;

    String title;

    Long categoryId;

    LocalDate meetingDate;

    String contactEmail;

    @JsonProperty("location")
    LocationRequestDto locationRequestDto;

    //todo 이미지 등록
}
