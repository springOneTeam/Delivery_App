package com.example.outsourcingproject.domain.review.dto;


public record ReviewRequest(Long orderId,
                            int rating,
                            String content
) {}
