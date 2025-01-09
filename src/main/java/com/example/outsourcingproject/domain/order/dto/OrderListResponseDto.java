package com.example.outsourcingproject.domain.order.dto;

import java.util.List;

public record OrderListResponseDto(List<OrderListDto> orderList) {

	public static OrderListResponseDto convertFrom(List<OrderListDto> orders){
		return new OrderListResponseDto(orders);
	}
}
