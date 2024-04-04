package com.hmpr.woofy.comment.service.impl;

import com.hmpr.woofy.board.entity.Board;
import com.hmpr.woofy.board.service.BoardService;
import com.hmpr.woofy.comment.dto.CommentResponse;
import com.hmpr.woofy.comment.dto.RegisterCommentRequest;
import com.hmpr.woofy.comment.dto.UpdateCommentRequest;
import com.hmpr.woofy.comment.entity.Comment;
import com.hmpr.woofy.comment.exception.CommentNotFoundException;
import com.hmpr.woofy.comment.repository.CommentRepository;
import com.hmpr.woofy.comment.service.CommentService;
import com.hmpr.woofy.user.entity.User;
import com.hmpr.woofy.user.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final BoardService boardService;



    public CommentServiceImpl(CommentRepository commentRepository, UserService userService, BoardService boardService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.boardService = boardService;
    }

    @Override
    public List<CommentResponse> getCommentsByBoard(Board board, User currentUser) {
        List<Comment> comments = commentRepository.findByBoard(board);
        return mapToCommentResponses(comments, currentUser);
    }

    private List<CommentResponse> mapToCommentResponses(List<Comment> comments, User currentUser) {
        return comments.stream()
                .map(comment -> mapToCommentResponse(comment, currentUser))
                .collect(Collectors.toList());
    }

    private CommentResponse mapToCommentResponse(Comment comment, User currentUser) {
        String isOwner = comment.getUser().getUserId().equals(currentUser.getUserId()) ? "Y" : "N";
        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .nickname(userService.getUserById(comment.getUser().getUserId()).getNickname())
                .content(comment.getContent())
                .isOwner(isOwner)
                .build();
    }

    @Transactional
    @Override
    public void saveComment(RegisterCommentRequest requestDto) {
        User user = userService.getUserById(requestDto.getUserId());
        Board board = boardService.getBoardById(requestDto.getBoardId());

        Comment comment = Comment.builder()
                .content(requestDto.getContent())
                .board(board)
                .user(user)
                .build();

        commentRepository.save(comment);
    }

    @Transactional
    @Override
    public void updateComment(Long commentId, UpdateCommentRequest requestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다. ID: " + commentId));

        comment.setContent(requestDto.getContent());

        commentRepository.save(comment);
    }


    @Transactional
    @Override
    public void deleteComment(Long commentId) {
        Comment comment = getCommentById(commentId);

        commentRepository.delete(comment);
    }

    private Comment getCommentById(Long commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다. ID: " + commentId));
        return comment;
    }

}
