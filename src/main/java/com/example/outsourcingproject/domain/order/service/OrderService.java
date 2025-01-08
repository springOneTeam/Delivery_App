package com.example.outsourcingproject.domain.order.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.outsourcingproject.domain.order.dto.CreateOrderRequestDto;
import com.example.outsourcingproject.domain.order.entity.Order;
import com.example.outsourcingproject.domain.order.repository.OrderRepository;
import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.store.repository.StoreRepository;
import com.example.outsourcingproject.exception.GlobalExceptionHandler;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final StoreRepository storeRepository;
	private final GlobalExceptionHandler globalExceptionHandler;

	public void createOrder(Long userId, CreateOrderRequestDto createOrderRequestDto) {
		/*Todo 예외처리 수정 예정*/
		Store store = storeRepository.findById(createOrderRequestDto.storeId())
			.orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
		/*Todo menu 추가 예정*/
		Order order = new Order();
		Order saveOrder=orderRepository.save(order);
	}
}
