package com.hmpr.woofy.Like.exception;

public class AlreadyLikedException extends RuntimeException {
    public AlreadyLikedException() {
        super("이미 좋아요가 되어 있습니다.");
    }
}