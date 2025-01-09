package com.example.outsourcingproject.domain.order.dto;

import com.example.outsourcingproject.domain.order.enums.OrderStatus;

public record ChangeOrderStatusRequestDto(String orderStatus) {
}
