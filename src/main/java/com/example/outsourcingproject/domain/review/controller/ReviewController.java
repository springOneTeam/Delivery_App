package com.example.outsourcingproject.domain.review.controller;


import com.example.outsourcingproject.domain.review.dto.ReviewRequest;
import com.example.outsourcingproject.domain.review.dto.ReviewResponse;
import com.example.outsourcingproject.domain.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }
    // 리뷰 작성
    @PostMapping("/orders/{orderId}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse createReview(@RequestBody ReviewRequest reviewRequest, @AuthenticationPrincipal Long userId) {
        return reviewService.createReview(reviewRequest, userId);
    }

    //리뷰조회(가게기준, 필터링 포함)
    @GetMapping("/store/{storeId}/reviews")
    public List<ReviewResponse> getReviewsByStore(@PathVariable Long storeId,
                                                  @RequestParam(required = false) Integer minRating,
                                                  @RequestParam(required = false) Integer maxRating) {
        if (minRating != null && maxRating != null) {
            return reviewService.getReviewsByStoreIdAndRating(storeId, minRating, maxRating);
        } else {
            return reviewService.getReviewsByStoreId(storeId);
        }
    }
}
