package com.example.outsourcingproject.domain.order.dto;

import java.time.LocalDateTime;

import com.example.outsourcingproject.domain.order.entity.Order;
import com.example.outsourcingproject.domain.order.enums.OrderStatus;

public record OrderListDto(Long orderId, OrderStatus orderStatus, LocalDateTime orderTime, int cart, int totalAmount,
						   String menuName, String storeName) {

	public static OrderListDto mapToDto(Order order) {
		return new OrderListDto(
			order.getOrderId(),
			order.getOrderStatus(),
			order.getOrdertime(),
			order.getCart(),
			order.getTotalAmount(),
			order.getMenu().getMenuName(),
			order.getStore().getStoreName());
	}
}
