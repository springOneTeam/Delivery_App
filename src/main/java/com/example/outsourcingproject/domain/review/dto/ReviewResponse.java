package com.example.outsourcingproject.domain.review.dto;

import java.time.LocalDateTime;

public record ReviewResponse(Long reviewId,         // 리뷰 ID
                             Long orderId,          // 주문 ID
                             Long userId,           // 작성자 ID
                             Long storeId,          // 가게 ID
                             int rating,            // 별점
                             String content,
                             LocalDateTime createdAt
) {
}
