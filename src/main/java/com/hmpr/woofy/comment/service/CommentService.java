package com.hmpr.woofy.comment.service;

import com.hmpr.woofy.board.entity.Board;
import com.hmpr.woofy.comment.dto.CommentResponse;
import com.hmpr.woofy.comment.dto.RegisterCommentRequest;
import com.hmpr.woofy.comment.dto.UpdateCommentRequest;
import com.hmpr.woofy.user.entity.User;

import java.util.List;

public interface CommentService {

    List<CommentResponse> getCommentsByBoard(Board board, User currentUser);

    void saveComment(RegisterCommentRequest requestDto);

    void updateComment(Long commentId, UpdateCommentRequest requestDto);

    void deleteComment(Long commentId);

}
