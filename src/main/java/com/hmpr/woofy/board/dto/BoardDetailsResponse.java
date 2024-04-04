package com.hmpr.woofy.board.dto;

import com.hmpr.woofy.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
public class BoardDetailsResponse {

    private Long boardId;

    private String title;

    private String nickName;

    private String categoryName;

    private LocationResponse location;

    private LocalDateTime registrationDate;

    private LocalDate meetingDate;

    private String contactEmail;

    private String content;

    private String imageUrl;

    private Long likeCount;

    private List<Comment> commentList;
}
