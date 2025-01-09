package com.example.outsourcingproject.domain.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.outsourcingproject.common.ApiResponse;
import com.example.outsourcingproject.domain.order.dto.ChangeOrderStatusRequestDto;
import com.example.outsourcingproject.domain.order.dto.ChangeOrderStatusResponseDto;
import com.example.outsourcingproject.domain.order.dto.CreateOrderRequestDto;
import com.example.outsourcingproject.domain.order.dto.CreateOrderResponseDto;
import com.example.outsourcingproject.domain.order.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	// 주문 생성
	@PostMapping
	public ResponseEntity<ApiResponse<CreateOrderResponseDto>> creatOrder(
		HttpServletRequest request,
		@RequestBody CreateOrderRequestDto createOrderRequestDto
	) {
		Long userId = (Long)request.getAttribute("userId");
		orderService.createOrder(userId, createOrderRequestDto);

		return new ResponseEntity<>(ApiResponse.success("음식을 주문했습니다."), HttpStatus.CREATED);
	}

	// 주문 상태 변경
	@PatchMapping("/{orderId}/status")
	public ResponseEntity<ApiResponse<ChangeOrderStatusResponseDto>> updateOrderStatus(
		HttpServletRequest request,
		@PathVariable Long orderId,
		@RequestBody ChangeOrderStatusRequestDto requestDto
	) {
		Long userId = (Long)request.getAttribute("userId");
		ChangeOrderStatusResponseDto responseDto = orderService.updateOrderStatus(userId, orderId, requestDto);

		return new ResponseEntity<>(
			ApiResponse.success("주문상태를 변경했습니다.", responseDto),
			HttpStatus.OK);
	}
}
