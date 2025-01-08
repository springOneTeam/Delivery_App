package com.example.outsourcingproject.domain.order.dto;

public record CreateOrderRequestDto(Long storeId, Long memberId, int cart) {
}
