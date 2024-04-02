package com.hmpr.woofy.Like.service.impl;

import com.hmpr.woofy.Like.dto.LikeRequest;
import com.hmpr.woofy.Like.entity.Like;
import com.hmpr.woofy.Like.exception.AlreadyLikedException;
import com.hmpr.woofy.Like.exception.LikeNotFoundException;
import com.hmpr.woofy.Like.repository.LikeRepository;
import com.hmpr.woofy.Like.service.LikeService;
import com.hmpr.woofy.board.entity.Board;
import com.hmpr.woofy.board.exception.BoardNotFoundException;
import com.hmpr.woofy.board.repository.BoardRepository;
import com.hmpr.woofy.user.entity.User;
import com.hmpr.woofy.user.exception.UserNotFoundException;
import com.hmpr.woofy.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeServiceImpl implements LikeService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;

    public LikeServiceImpl(UserRepository userRepository, BoardRepository boardRepository, LikeRepository likeRepository) {
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
        this.likeRepository = likeRepository;
    }

    @Transactional
    @Override
    public void insertLike(LikeRequest requestDto) {
        User user = findUserById(requestDto.getUserId());
        Board board = findBoardById(requestDto.getBoardId());
        if (likeRepository.findByUserAndBoard(user, board).isPresent()) {
            throw new AlreadyLikedException();
        }
        saveLike(user, board);
    }

    @Transactional
    @Override
    public void delete(LikeRequest requestDto) {
        User user = findUserById(requestDto.getUserId());
        Board board = findBoardById(requestDto.getBoardId());
        Like like = likeRepository.findByUserAndBoard(user, board)
                .orElseThrow(() -> new LikeNotFoundException());
        likeRepository.delete(like);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저 정보를 찾을 수 없습니다 Id : " + userId));
    }

    private Board findBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("게시판 정보를 찾을 수 없습니다. Id : " + boardId));
    }

    private void saveLike(User user, Board board) {
        Like like = Like.builder()
                .board(board)
                .user(user)
                .build();
        likeRepository.save(like);
    }
}
