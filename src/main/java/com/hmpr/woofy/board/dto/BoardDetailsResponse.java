package com.hmpr.woofy.board.dto;

import com.hmpr.woofy.board.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class BoardDetailsResponse {

    private Long board_id;

    private String title;

    private String userName;

    private String categoryName;

    private String location;

    private LocalDate deadlineDate;

    private LocalDate registrationDate;

    private LocalDate executionDate;

    private String contactEmail;

    private String imageUrl;

    private List<Comment> commentList;
}
