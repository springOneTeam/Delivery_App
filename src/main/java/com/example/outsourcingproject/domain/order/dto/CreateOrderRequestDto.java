package com.example.outsourcingproject.domain.order.dto;

public record CreateOrderRequestDto(Long storeId, Long menuId, int cart) {
}
