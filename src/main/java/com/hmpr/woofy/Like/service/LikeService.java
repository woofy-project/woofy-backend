package com.hmpr.woofy.Like.service;

import com.hmpr.woofy.Like.dto.LikeRequest;

public interface LikeService {

    void insertLike(LikeRequest requestDto);

    void delete(LikeRequest requestDto);
}
