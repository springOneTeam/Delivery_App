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
import com.example.outsourcingproject.exception.ErrorCode;
import com.example.outsourcingproject.exception.auth.UnauthorizedException;
import com.example.outsourcingproject.exception.common.BusinessException;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {
	private final StoreRepository storeRepository;
	private final UserRepository userRepository;

	private static final int MAX_STORES_PER_OWNER = 3;

	public StoreCreateResponseDto createStore(StoreCreateRequestDto requestDto, Long userId) {
		// 사용자 조회
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

		// 가게 개수 체크
		validateStoreCount(user);

		// 가게 생성 - 여기서 자동으로 OWNER 권한 체크됨 (DTO의 팩토리 메서드에서)
		Store store = StoreCreateRequestDto.toEntity(requestDto, user);
		Store savedStore = storeRepository.save(store);

		return StoreCreateResponseDto.from(savedStore);
	}

	private void validateStoreCount(User owner) {
		long storeCount = storeRepository.countByOwner(owner);
		if (storeCount >= MAX_STORES_PER_OWNER) {
			throw new BusinessException(ErrorCode.INVALID_ACCESS, "가게는 최대 3개까지만 등록 가능합니다.");
		}
	}
	public Store findStoreById(Long storeId) {
		return storeRepository.findById(storeId)
			.orElseThrow(() -> new EntityNotFoundException("가게를 찾을 수 없습니다."));
	}
}