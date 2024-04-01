package com.hmpr.woofy.board.controller;

import com.hmpr.woofy.board.dto.*;
import com.hmpr.woofy.board.service.BoardService;
import com.hmpr.woofy.common.dto.CommonApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/list")
    public ResponseEntity<CommonApiResponse<List<BoardListResponse>>> getBoardList(@RequestBody BoardListRequest requestDto) {
        Page<BoardListResponse> boardPage = boardService.getBoardList(requestDto);
        List<BoardListResponse> boardList = boardPage.getContent();
        return ResponseEntity.ok(CommonApiResponse.createSuccess("페이징 공고 조회 성공", boardList));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<CommonApiResponse<BoardDetailsResponse>> getBoardDetails(@PathVariable Long boardId) {
        BoardDetailsResponse boardDetails = boardService.getBoardDetails(boardId);
        return ResponseEntity.ok(CommonApiResponse.createSuccess("공고 정보 조회 성공", boardDetails));
    }

    @PostMapping("/register")
    public ResponseEntity<CommonApiResponse> registerBoard(@RequestBody RegisterBoardRequest requestDto) {
            boardService.registerBoard(requestDto);
            return ResponseEntity.ok(CommonApiResponse.createSuccessWithNoContent("공고 등록 성공"));
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<CommonApiResponse> updateBoard(@PathVariable Long boardId, @RequestBody UpdateBoardRequest requestDto) {
        boardService.updateBoard(boardId, requestDto);
        return ResponseEntity.ok(CommonApiResponse.createSuccessWithNoContent("공고 수정 성공"));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<CommonApiResponse> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok(CommonApiResponse.createSuccessWithNoContent("공고 삭제 성공"));
    }
}