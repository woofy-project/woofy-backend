package com.hmpr.woofy.board.controller;

import com.hmpr.woofy.board.dto.BoardDetailsResponse;
import com.hmpr.woofy.board.dto.RegisterBoardRequestDto;
import com.hmpr.woofy.board.service.BoardService;
import com.hmpr.woofy.common.dto.CommonApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<CommonApiResponse<BoardDetailsResponse>> getBoardDetails(@PathVariable Long boardId) {
        try {
            BoardDetailsResponse boardDetails = boardService.getBoardDetails(boardId);
            CommonApiResponse<BoardDetailsResponse> successResponse = CommonApiResponse.<BoardDetailsResponse>builder()
                    .status("200")
                    .message("게시판 세부 정보 조회 성공")
                    .data(boardDetails)
                    .build();
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            return null;
        }
    }


    @PostMapping("/register")
    public ResponseEntity<CommonApiResponse> registerBoard(@RequestBody RegisterBoardRequestDto requestDto) {
        try {
            boardService.registerBoard(requestDto);
            return ResponseEntity.ok(new CommonApiResponse("success", "게시판 등록 성공", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CommonApiResponse("success", "게시판 등록 오류", null));
        }
    }
}