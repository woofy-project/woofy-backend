package com.hmpr.woofy.board.service.impl;

import com.hmpr.woofy.Like.repository.LikeRepository;
import com.hmpr.woofy.board.dto.*;
import com.hmpr.woofy.board.entity.*;
import com.hmpr.woofy.board.exception.BoardNotFoundException;
import com.hmpr.woofy.board.repository.BoardRepository;
import com.hmpr.woofy.board.repository.LocationRepository;
import com.hmpr.woofy.board.service.BoardService;
import com.hmpr.woofy.comment.dto.CommentResponse;
import com.hmpr.woofy.comment.service.CommentService;
import com.hmpr.woofy.user.entity.QUser;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    private final JPAQueryFactory queryFactory;
    private final BoardRepository boardRepository;
    private final LocationRepository locationRepository;
    private final LikeRepository likeRepository;
    private final CommentService commentService;

    public BoardServiceImpl(JPAQueryFactory queryFactory, BoardRepository boardRepository, LocationRepository locationRepository, LikeRepository likeRepository, CommentService commentService) {
        this.queryFactory = queryFactory;
        this.boardRepository = boardRepository;
        this.locationRepository = locationRepository;
        this.likeRepository = likeRepository;
        this.commentService = commentService;
    }

    /**
     * 카테고리별, 지정된 페이지 번호로 페이징된 공고 리스트를 가져옴
     *
     * @param requestDto 카테고리Id,페이지번호,페이지 크기
     * @return 페이징된 리스트
     */
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

    /**
     * 공고 상세 정보를 가져옴
     *
     * @param boardId 공고 ID
     * @return 공고 상세 정보
     * @throws BoardNotFoundException 해당 ID의 공고가 없을 때 발생
     */
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
        List<CommentResponse> comments = commentService.getCommentsByBoard(board);
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
                .commentList(comments)
                .build();
    }

    /**
     * 공고 등록 처리
     *
     * @param requestDto 등록할 공고 정보
     */
    @Override
    public void registerBoard(RegisterBoardRequest requestDto) {

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

    private Long saveLocation(LocationRequest locationRequest) {
        Location location = Location.builder()
                .streetAddress(locationRequest.getStreetAddress())
                .detail(locationRequest.getDetail())
                .build();

        Location savedLocation = locationRepository.save(location);

        return savedLocation.getLocationId();
    }

    /**
     * 공고 정보 수정 처리
     *
     * @param boardId    수정할 공고 ID
     * @param requestDto 수정할 공고 정보
     * @throws BoardNotFoundException 해당 ID의 공고가 없을 때 발생
     */
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

    /**
     * 공고 삭제 처리
     *
     * @param boardId 삭제할 공고 ID
     * @throws BoardNotFoundException 해당 ID의 공고가 없을 때 발생
     */
    @Override
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("게시판을 찾을 수 없습니다. ID: " + boardId));
        boardRepository.delete(board);
    }

    /**
     * 공고에 등록된 좋아요 수를 가져옴
     *
     * @param boardId 공고 ID
     * @return 해당 공고의 좋아요 수
     */
    private long countLikeOfBoard(Long boardId) {
        return likeRepository.countByBoardId(boardId);
    }

    /**
     * 공고아이디값으로 공고 객체를 가져옴
     *
     * @param boardId 공고 ID
     * @return 공고 객체
     */
    @Override
    public Board getBoardById(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("게시판을 찾을 수 없습니다. ID: " + boardId));
        return board;
    }
}