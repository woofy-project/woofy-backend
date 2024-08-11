package com.hmpr.woofy.comment.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommentResponse {

    private Long commentId;

    private String nickname;

    private String content;

    private String isOwner;

}
