package com.hmpr.woofy.Like.repository;

import com.hmpr.woofy.Like.entity.Like;
import com.hmpr.woofy.board.entity.Board;
import com.hmpr.woofy.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository <Like, Long> {

    Optional<Like> findByUserAndBoard(User user, Board board);

    Long countByBoard(Board board);
}
