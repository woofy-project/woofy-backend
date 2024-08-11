package com.hmpr.woofy.board.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BoardListResponse {

    private Long boardId;

    private String title;

    private LocationResponse location;

    private String nickname;

}
