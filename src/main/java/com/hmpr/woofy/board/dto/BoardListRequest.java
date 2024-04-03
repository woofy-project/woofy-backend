package com.hmpr.woofy.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardListRequest {

    private Long categoryId;

    private int page;

    private int size;
}
