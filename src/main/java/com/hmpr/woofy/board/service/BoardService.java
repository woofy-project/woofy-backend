package com.hmpr.woofy.board.service;

import com.hmpr.woofy.board.dto.BoardDetailsResponse;
import com.hmpr.woofy.board.dto.RegisterBoardRequestDto;

public interface BoardService {

    BoardDetailsResponse getBoardDetails(Long boardId);

    void registerBoard(RegisterBoardRequestDto requestDto);

    void deleteBoard(Long boardId);

}
