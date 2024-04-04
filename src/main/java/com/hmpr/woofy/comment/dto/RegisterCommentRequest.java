package com.hmpr.woofy.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class RegisterCommentRequest {

    private Long boardId;

    private Long userId;

    private String content;

}
