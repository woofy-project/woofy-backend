package com.hmpr.woofy.Like.controller;

import com.hmpr.woofy.Like.dto.LikeRequest;
import com.hmpr.woofy.Like.service.LikeService;
import com.hmpr.woofy.common.dto.CommonApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/like")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping
    public ResponseEntity<CommonApiResponse> insertLike(@RequestBody LikeRequest requestDto){
        likeService.insertLike(requestDto);
        return ResponseEntity.ok(CommonApiResponse.createSuccessWithNoContent("좋아요 등록 성공"));
    }

    @PostMapping
    public ResponseEntity<CommonApiResponse> deleteLike(@RequestBody LikeRequest requestDto){
        likeService.delete(requestDto);
        return ResponseEntity.ok(CommonApiResponse.createSuccessWithNoContent("좋아요 삭제 성공"));
    }

}
