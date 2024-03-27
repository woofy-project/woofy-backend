package com.hmpr.woofy.board.service.impl;

import com.hmpr.woofy.board.dto.BoardDetailsResponse;
import com.hmpr.woofy.board.dto.LocationRequestDto;
import com.hmpr.woofy.board.dto.RegisterBoardRequestDto;
import com.hmpr.woofy.board.entity.Board;
import com.hmpr.woofy.board.entity.Location;
import com.hmpr.woofy.board.repository.BoardRepository;
import com.hmpr.woofy.board.repository.LocationRepository;
import com.hmpr.woofy.board.service.BoardService;
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
        return null;
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

        return savedLocation.getId();
    }

}