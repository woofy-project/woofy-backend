package com.hmpr.woofy.board.service.impl;

import com.hmpr.woofy.board.dto.BoardDetailsResponse;
import com.hmpr.woofy.board.dto.LocationRequestDto;
import com.hmpr.woofy.board.dto.LocationResponseDto;
import com.hmpr.woofy.board.dto.RegisterBoardRequestDto;
import com.hmpr.woofy.board.entity.*;
import com.hmpr.woofy.board.exception.BoardNotFoundException;
import com.hmpr.woofy.board.repository.BoardRepository;
import com.hmpr.woofy.board.repository.LocationRepository;
import com.hmpr.woofy.board.service.BoardService;
import com.hmpr.woofy.user.entity.QUser;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BoardServiceImpl implements BoardService {

    private final JPAQueryFactory queryFactory;
    private final BoardRepository boardRepository;
    private final LocationRepository locationRepository;

    public BoardServiceImpl(JPAQueryFactory queryFactory, BoardRepository boardRepository, LocationRepository locationRepository) {
        this.queryFactory = queryFactory;
        this.boardRepository = boardRepository;
        this.locationRepository = locationRepository;
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
        LocationResponseDto locationResponseDto = LocationResponseDto.builder()
                .streetAddress(result.get(QLocation.location.streetAddress))
                .detail(result.get(QLocation.location.detail))
                .build();

        return BoardDetailsResponse.builder()
                .boardId(boardId)
                .title(board.getTitle())
                .nickName(nickname)
                .categoryName(categoryName)
                .location(locationResponseDto)
                .registrationDate(board.getRegistrationDate())
                .meetingDate(board.getMeetingDate())
                .contactEmail(board.getContactEmail())
                .build();
    }

    @Override
    public void registerBoard(RegisterBoardRequestDto requestDto){

        Long userId = requestDto.getUserId();
        String title = requestDto.getTitle();
        String contactEmail = requestDto.getContactEmail();
        Long categoryId = requestDto.getCategoryId();
        LocalDate meetingDate = requestDto.getMeetingDate();
        // todo: 이미지 등록에 대한 처리 추가

        Board newBoard = Board.builder()
                .userId(userId)
                .title(title)
                .categoryId(categoryId)
                .meetingDate(meetingDate)
                .contactEmail(contactEmail)
                .locationId(saveLocation(requestDto.getLocationRequestDto()))
                // todo: 이미지 등록에 대한 처리 추가
                .build();
        boardRepository.save(newBoard);
    }

    private Long saveLocation(LocationRequestDto locationRequestDto){
        Location location = Location.builder()
                .streetAddress(locationRequestDto.getStreetAddress())
                .detail(locationRequestDto.getDetail())
                .build();

        Location savedLocation = locationRepository.save(location);

        return savedLocation.getLocationId();
    }

}