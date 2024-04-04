package com.hmpr.woofy.comment.controller;

import com.hmpr.woofy.comment.dto.RegisterCommentRequest;
import com.hmpr.woofy.comment.dto.UpdateCommentRequest;
import com.hmpr.woofy.comment.service.CommentService;
import com.hmpr.woofy.common.dto.CommonApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommonApiResponse> saveComment(@RequestBody RegisterCommentRequest requestDto) {
        commentService.saveComment(requestDto);
        return ResponseEntity.ok(CommonApiResponse.createSuccessWithNoContent("댓글 저장 성공"));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommonApiResponse> updateComment(@PathVariable Long commentId, @RequestBody UpdateCommentRequest requestDto) {
        commentService.updateComment(commentId, requestDto);
        return ResponseEntity.ok(CommonApiResponse.createSuccessWithNoContent("댓글 수정 성공"));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommonApiResponse> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(CommonApiResponse.createSuccessWithNoContent("댓글 삭제 성공"));
    }
}