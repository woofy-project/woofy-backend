package com.hmpr.woofy.board.controller;

import com.hmpr.woofy.board.dto.BoardDetailsResponse;
import com.hmpr.woofy.board.dto.RegisterBoardRequestDto;
import com.hmpr.woofy.board.exception.BoardNotFoundException;
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
            return ResponseEntity.ok(CommonApiResponse.createSuccess("게시판 정보 조회 성공", boardDetails));
        } catch (BoardNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(CommonApiResponse.createError("게시판 조회 오류"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<CommonApiResponse> registerBoard(@RequestBody RegisterBoardRequestDto requestDto) {
        try {
            boardService.registerBoard(requestDto);
            return ResponseEntity.ok(CommonApiResponse.createSuccessWithNoContent("게시판 등록 성공"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CommonApiResponse.createError("게시판 등록 오류"));
        }
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<CommonApiResponse> deleteBoard(@PathVariable Long boardId) {
        try {
            boardService.deleteBoard(boardId);
            return ResponseEntity.ok(CommonApiResponse.createSuccessWithNoContent("게시판 삭제 성공"));
        } catch (BoardNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CommonApiResponse.createError("게시판을 찾을 수 없습니다."));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CommonApiResponse.createError("게시판 삭제 중 오류가 발생했습니다."));
        }
    }
}