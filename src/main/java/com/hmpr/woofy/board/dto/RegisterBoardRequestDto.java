package com.hmpr.woofy.board.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterBoardRequestDto {

    private Long userId;

    private String title;

    private Long categoryId;

    private LocalDate meetingDate;

    private String contactEmail;

    private String content;

    @JsonProperty("location")
    private LocationRequestDto locationRequestDto;

}
