package com.hmpr.woofy.config.exception;

import com.hmpr.woofy.board.exception.BoardNotFoundException;
import com.hmpr.woofy.common.dto.CommonApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.badRequest().body("서버에서 오류가 발생했습니다.");
    }

    @ExceptionHandler(BoardNotFoundException.class)
    protected ResponseEntity<CommonApiResponse> handleBoardNotFoundException(BoardNotFoundException ex) {
        CommonApiResponse response = CommonApiResponse.createError(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
