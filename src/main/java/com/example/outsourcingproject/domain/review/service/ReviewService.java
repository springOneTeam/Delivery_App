package com.example.outsourcingproject.domain.review.service;

import com.example.outsourcingproject.domain.review.dto.ReviewRequest;
import com.example.outsourcingproject.domain.review.dto.ReviewResponse;
import com.example.outsourcingproject.domain.review.entity.Review;
import com.example.outsourcingproject.domain.review.repository.ReviewRepository;
import com.example.outsourcingproject.domain.store.service.StoreService;
import com.example.outsourcingproject.domain.user.service.UserService;
import com.example.outsourcingproject.domain.order.service.OrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService; // 유저 서비스
    private final OrderService orderService; // 주문 서비스
    private final StoreService storeService; // 가게 서비스

    public ReviewService(
            ReviewRepository reviewRepository,
            UserService userService,
            OrderService orderService,
            StoreService storeService) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.orderService = orderService;
        this.storeService = storeService;
    }

    public ReviewResponse createReview(ReviewRequest reviewRequest, Long userId) {
        // 필요한 데이터 유효성 검증
        orderService.validateOrder(reviewRequest.orderId()); // 주문 존재 여부 확인
        if (!orderService.isOrderCompleted(reviewRequest.orderId())) {
            throw new IllegalArgumentException("주문이 완료된 상태에서만 리뷰를 작성할 수 있습니다.");
        }
        Long storeId = orderService.findStoreIdByOrderId(reviewRequest.orderId()); // 주문에 연결된 가게 ID 가져오기
        userService.validateUser(userId); // 사용자 존재 여부 확인

        // Review 엔티티 생성
        Review review = new Review(
                null, // reviewId는 자동 생성
                reviewRequest.orderId(),
                userId,
                storeId,
                reviewRequest.rating(),
                reviewRequest.content(),
                LocalDateTime.now()
        );

        // 저장
        Review savedReview = reviewRepository.save(review);

        // 응답 객체 생성
        return new ReviewResponse(
                savedReview.getReviewId(),
                savedReview.getOrderId(),
                savedReview.getUserId(),
                savedReview.getStoreId(),
                savedReview.getRating(),
                savedReview.getContent(),
                savedReview.getCreatedAt()
        );
    }
    public List<ReviewResponse> getReviewsByStoreId(Long storeId) {
        // storeId를 기준으로 리뷰 조회
        List<Review> reviews = reviewRepository.findByStoreId(storeId);

        // 리뷰 리스트를 ReviewResponse로 변환
        return reviews.stream()
                .map(review -> new ReviewResponse(
                        review.getReviewId(),
                        review.getOrderId(),
                        review.getUserId(),
                        review.getStoreId(),
                        review.getRating(),
                        review.getContent(),
                        review.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public List<ReviewResponse> getReviewsByStoreIdAndRating(Long storeId, int minRating, int maxRating) {
        // storeId와 별점 범위를 기준으로 리뷰 조회
        List<Review> reviews = reviewRepository.findByStoreIdAndRatingBetween(storeId, minRating, maxRating);

        // 리뷰 리스트를 ReviewResponse로 변환
        return reviews.stream()
                .map(review -> new ReviewResponse(
                        review.getReviewId(),
                        review.getOrderId(),
                        review.getUserId(),
                        review.getStoreId(),
                        review.getRating(),
                        review.getContent(),
                        review.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }




}
