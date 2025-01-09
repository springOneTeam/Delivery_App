package com.example.outsourcingproject.domain.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.outsourcingproject.common.ApiResponse;
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

	@PostMapping
	public ResponseEntity<ApiResponse<CreateOrderResponseDto>> creatOrder(
		HttpServletRequest request,
		@RequestBody CreateOrderRequestDto createOrderRequestDto
	) {
		Long userId = (Long)request.getAttribute("userId");
		orderService.createOrder(userId, createOrderRequestDto);
		return new ResponseEntity<>(ApiResponse.success("음식을 주문했습니다."), HttpStatus.CREATED);
	}
}
