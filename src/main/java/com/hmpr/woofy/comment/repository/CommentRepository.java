package com.hmpr.woofy.comment.repository;

import com.hmpr.woofy.board.entity.Board;
import com.hmpr.woofy.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoard(Board board);
}
