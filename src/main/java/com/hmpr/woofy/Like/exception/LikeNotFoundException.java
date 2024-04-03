package com.hmpr.woofy.Like.exception;

public class LikeNotFoundException extends RuntimeException {
    public LikeNotFoundException() {
        super("좋아요 기록이 없습니다.");
    }
}