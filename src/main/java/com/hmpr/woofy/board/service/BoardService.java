package com.hmpr.woofy.board.service;

import com.hmpr.woofy.board.dto.*;
import org.springframework.data.domain.Page;

public interface BoardService {

    Page<BoardListResponse> getBoardList(BoardListRequest requestDto);

    BoardDetailsResponse getBoardDetails(Long boardId);

    void registerBoard(RegisterBoardRequest requestDto);

    void deleteBoard(Long boardId);

    void updateBoard(Long boardId, UpdateBoardRequest requestDto);
}
