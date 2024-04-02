package com.hmpr.woofy.board.service.impl;

import com.hmpr.woofy.Like.repository.LikeRepository;
import com.hmpr.woofy.board.dto.*;
import com.hmpr.woofy.board.entity.*;
import com.hmpr.woofy.board.exception.BoardNotFoundException;
import com.hmpr.woofy.board.repository.BoardRepository;
import com.hmpr.woofy.board.repository.LocationRepository;
import com.hmpr.woofy.board.service.BoardService;
import com.hmpr.woofy.user.entity.QUser;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BoardServiceImpl implements BoardService {

    private final JPAQueryFactory queryFactory;
    private final BoardRepository boardRepository;
    private final LocationRepository locationRepository;
    private final LikeRepository likeRepository;

    public BoardServiceImpl(JPAQueryFactory queryFactory, BoardRepository boardRepository, LocationRepository locationRepository, LikeRepository likeRepository) {
        this.queryFactory = queryFactory;
        this.boardRepository = boardRepository;
        this.locationRepository = locationRepository;
        this.likeRepository = likeRepository;
    }

    @Override
    public Page<BoardListResponse> getBoardList(BoardListRequest requestDto) {
        Pageable pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize());
        Page<Board> boardPage = boardRepository.findAll(pageable);
        return boardPage.map(this::mapToBoardListResponse);
    }

    private BoardListResponse mapToBoardListResponse(Board board) {
        return BoardListResponse.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .nickname(board.getUser().getNickname())
                .build();
    }

    @Override
    public BoardDetailsResponse getBoardDetails(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("게시판을 찾을 수 없습니다. ID: " + boardId));

        QBoard qBoard = QBoard.board;
        QUser qUser = QUser.user;
        QCategory qCategory = QCategory.category;
        QLocation qLocation = QLocation.location;

        Tuple result = queryFactory
                .select(qBoard, qUser.nickname, qCategory.categoryName)
                .from(qBoard)
                .join(qUser).on(qBoard.userId.eq(qUser.userId))
                .join(qCategory).on(qBoard.categoryId.eq(qCategory.categoryId))
                .join(qLocation).on(qBoard.locationId.eq(qLocation.locationId))
                .where(qBoard.boardId.eq(boardId))
                .fetchOne();

        BoardDetailsResponse boardDetailsResponse = mapToBoardDetailsResponse(result, boardId);

        return boardDetailsResponse;
    }

    private BoardDetailsResponse mapToBoardDetailsResponse(Tuple result, Long boardId) {
        Board board = result.get(QBoard.board);
        String nickname = result.get(QUser.user.nickname);
        String categoryName = result.get(QCategory.category.categoryName);
        LocationResponse locationResponse = LocationResponse.builder()
                .streetAddress(result.get(QLocation.location.streetAddress))
                .detail(result.get(QLocation.location.detail))
                .build();

        return BoardDetailsResponse.builder()
                .boardId(boardId)
                .title(board.getTitle())
                .nickName(nickname)
                .categoryName(categoryName)
                .location(locationResponse)
                .registrationDate(board.getRegistrationDate())
                .meetingDate(board.getMeetingDate())
                .contactEmail(board.getContactEmail())
                .content(board.getContent())
                .likeCount(countLikeOfBoard(boardId))
                .build();
    }

    @Override
    public void registerBoard(RegisterBoardRequest requestDto){

        Long userId = requestDto.getUserId();
        String title = requestDto.getTitle();
        String contactEmail = requestDto.getContactEmail();
        Long categoryId = requestDto.getCategoryId();
        LocalDate meetingDate = requestDto.getMeetingDate();
        String content = requestDto.getContent();

        Board newBoard = Board.builder()
                .userId(userId)
                .title(title)
                .categoryId(categoryId)
                .meetingDate(meetingDate)
                .contactEmail(contactEmail)
                .content(content)
                .locationId(saveLocation(requestDto.getLocationRequest()))
                .build();
        boardRepository.save(newBoard);
    }

    private Long saveLocation(LocationRequest locationRequest){
        Location location = Location.builder()
                .streetAddress(locationRequest.getStreetAddress())
                .detail(locationRequest.getDetail())
                .build();

        Location savedLocation = locationRepository.save(location);

        return savedLocation.getLocationId();
    }

    @Override
    public void updateBoard(Long boardId, UpdateBoardRequest requestDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("게시판을 찾을 수 없습니다. ID: " + boardId));

        board.setTitle(requestDto.getTitle());
        board.setCategoryId(requestDto.getCategoryId());
        board.setMeetingDate(requestDto.getMeetingDate());
        board.setContactEmail(requestDto.getContactEmail());
        board.setContent(requestDto.getContent());

        boardRepository.save(board);
    }

    @Override
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("게시판을 찾을 수 없습니다. ID: " + boardId));
        boardRepository.delete(board);
    }

    private long countLikeOfBoard(Long boardId) {
        return likeRepository.countByBoardId(boardId);
    }
}