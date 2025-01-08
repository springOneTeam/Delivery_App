package com.example.outsourcingproject.domain.order.service;

import org.springframework.stereotype.Service;

import com.example.outsourcingproject.domain.menu.entity.Menu;
import com.example.outsourcingproject.domain.menu.repository.MenuRepository;
import com.example.outsourcingproject.domain.order.dto.CreateOrderRequestDto;
import com.example.outsourcingproject.domain.order.entity.Order;
import com.example.outsourcingproject.domain.order.enums.OrderStatus;
import com.example.outsourcingproject.domain.order.repository.OrderRepository;
import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.store.repository.StoreRepository;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.repository.UserRepository;
import com.example.outsourcingproject.exception.ErrorCode;
import com.example.outsourcingproject.exception.GlobalExceptionHandler;
import com.example.outsourcingproject.exception.common.BusinessException;
import com.example.outsourcingproject.exception.common.MenuNotFoundException;
import com.example.outsourcingproject.exception.common.StoreNotFoundException;
import com.example.outsourcingproject.exception.common.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final StoreRepository storeRepository;
	private final MenuRepository menuRepository;
	private final UserRepository userRepository;
	private final GlobalExceptionHandler globalExceptionHandler;

	// 주문 생성
	public void createOrder(Long userId, CreateOrderRequestDto createOrderRequestDto) {
		Store store = findStoreByIdOrElseThrow(createOrderRequestDto.storeId());
		Menu menu = findMenuByIdOrElseThrow(createOrderRequestDto.menuId());
		User user = findUserByIdOrElseThrow(userId);

		// 주문 조건 불만족
		if(menu.getPrice()<store.getMinOrderAmount()){
			throw new BusinessException(ErrorCode.INVALID_TOTALAMOUNT);
		}
		/*if(){
			throw new BusinessException(ErrorCode.INVALID_ORDER_TIME);
		}*/

		// 주문 생성 시 일단 보류 상태로 표시
		Order order = new Order(user, store, menu, OrderStatus.PENDING, 1);

		Order saveOrder=orderRepository.save(order);
	}

	/* 예외처리 */
	private Store findStoreByIdOrElseThrow(Long storeId) {
		return storeRepository.findById(storeId)
			.orElseThrow(StoreNotFoundException::new);
	}

	private Menu findMenuByIdOrElseThrow(Long menuId){
		return menuRepository.findById(menuId)
			.orElseThrow(MenuNotFoundException::new);
	}

	private User findUserByIdOrElseThrow(Long userId){
		return userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);
	}


}
