package com.example.outsourcingproject.domain.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.example.outsourcingproject.domain.order.dto.OrderListResponseDto;
import com.example.outsourcingproject.domain.order.entity.Order;
import com.example.outsourcingproject.domain.order.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	// 주문 생성
	@PostMapping
	public ResponseEntity<ApiResponse<CreateOrderResponseDto>> creatOrder(
		@AuthenticationPrincipal Long userId,
		@Valid @RequestBody CreateOrderRequestDto createOrderRequestDto
	) {
		orderService.createOrder(userId, createOrderRequestDto);

		return new ResponseEntity<>(ApiResponse.success("음식을 주문했습니다."), HttpStatus.CREATED);
	}

	// 주문 상태 변경
	@PatchMapping("/{orderId}/status")
	@PreAuthorize("hasRole('OWNER')") // OWNER 권한을 가진 사용자만 접근 가능
	public ResponseEntity<ApiResponse<ChangeOrderStatusResponseDto>> updateOrderStatus(
		@AuthenticationPrincipal Long userId,
		@PathVariable Long orderId,
		@Valid @RequestBody ChangeOrderStatusRequestDto requestDto
	) {
		Order order = orderService.updateOrderStatus(userId, orderId, requestDto);
		ChangeOrderStatusResponseDto responseDto = new ChangeOrderStatusResponseDto(order.getOrderStatus());

		return new ResponseEntity<>(
			ApiResponse.success("주문상태를 변경했습니다.", responseDto), HttpStatus.OK);
	}

	// 주문 내역 조회
	@GetMapping
	public ResponseEntity<ApiResponse<OrderListResponseDto>> findAllOrders(
		@AuthenticationPrincipal Long userId
	) {
		OrderListResponseDto responseDto = orderService.findAllOrders(userId);

		return new ResponseEntity<>(
			ApiResponse.success("주문 내역:", responseDto), HttpStatus.OK);
	}
}
