package com.example.outsourcingproject.domain.store.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.outsourcingproject.domain.store.dto.StoreCreateRequestDto;
import com.example.outsourcingproject.domain.store.dto.StoreCreateResponseDto;
import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.store.repository.StoreRepository;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.enums.UserRoleEnum;
import com.example.outsourcingproject.domain.user.repository.UserRepository;
import com.example.outsourcingproject.exception.auth.UnauthorizedException;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {
	private static final int MAX_STORES_PER_OWNER = 3;

	private final StoreRepository storeRepository;
	private final UserRepository userRepository;

	@Transactional
	public StoreCreateResponseDto createStore(Long userId, StoreCreateRequestDto requestDto) {
		// 사용자 조회
		User owner = userRepository.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

		// 사장님 권한 확인
		validateOwnerRole(owner);

		// 가게 개수 제한 확인
		validateStoreCount(owner);

		// 가게 생성
		Store store = requestDto.toEntity(owner);
		Store savedStore = storeRepository.save(store);

		return StoreCreateResponseDto.of(savedStore);
	}

	private void validateOwnerRole(User user) {
		if (user.getRole() != UserRoleEnum.OWNER) {  // UserRole -> UserRoleEnum으로 변경
			throw new UnauthorizedException("사장님만 가게를 등록할 수 있습니다.");
		}
	}

	private void validateStoreCount(User owner) {
		int activeStoresCount = storeRepository.countActiveStoresByOwner(owner);
		if (activeStoresCount >= MAX_STORES_PER_OWNER) {
			throw new IllegalStateException("사장님당 최대 " + MAX_STORES_PER_OWNER + "개의 가게만 등록할 수 있습니다.");
		}
	}

	public Store findStoreById(Long storeId) {
		return storeRepository.findById(storeId)
			.orElseThrow(() -> new EntityNotFoundException("가게를 찾을 수 없습니다."));
	}
}