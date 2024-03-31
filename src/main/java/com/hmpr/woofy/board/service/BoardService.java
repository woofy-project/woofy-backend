package com.hmpr.woofy.board.service;

import com.hmpr.woofy.board.dto.BoardDetailsResponse;
import com.hmpr.woofy.board.dto.RegisterBoardRequest;
import com.hmpr.woofy.board.dto.UpdateBoardRequest;

public interface BoardService {

    BoardDetailsResponse getBoardDetails(Long boardId);

    void registerBoard(RegisterBoardRequest requestDto);

    void deleteBoard(Long boardId);

    void updateBoard(Long boardId, UpdateBoardRequest requestDto);
}
