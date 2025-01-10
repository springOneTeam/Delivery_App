package com.example.outsourcingproject.domain.store.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.outsourcingproject.common.ApiResponse;
import com.example.outsourcingproject.domain.store.dto.StoreCloseResponseDto;
import com.example.outsourcingproject.domain.store.dto.StoreCreateRequestDto;
import com.example.outsourcingproject.domain.store.dto.StoreCreateResponseDto;
import com.example.outsourcingproject.domain.store.dto.StoreDetailResponseDto;
import com.example.outsourcingproject.domain.store.dto.StoreListResponseDto;
import com.example.outsourcingproject.domain.store.dto.StoreUpdateRequestDto;
import com.example.outsourcingproject.domain.store.dto.StoreUpdateResponseDto;
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

	@Transactional  // 여기에 별도로 Transactional 추가 (readOnly = false가 기본값)
	public StoreCreateResponseDto createStore(StoreCreateRequestDto requestDto, Long userId) {
		// 사용자 조회
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

		// 가게 개수 체크
		validateStoreCount(user);

		// 가게 생성
		Store store = StoreCreateRequestDto.toEntity(requestDto, user);
		Store savedStore = storeRepository.save(store);

		return StoreCreateResponseDto.from(savedStore);
	}

	@Transactional
	public StoreUpdateResponseDto updateStore(Long storeId, Long userId, StoreUpdateRequestDto requestDto) {
		// 가게 존재 여부 확인
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));

		// 가게 주인 확인
		// if (!store.getOwner().getUserId().equals(userId)) {
		// 	throw new BusinessException(ErrorCode.FORBIDDEN_ACCESS, "해당 가게의 수정 권한이 없습니다.");
		// }

		// 사용자의 OWNER 권한 확인
		// User user = userRepository.findById(userId)
		// 	.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
		// if (user.getRole() != UserRoleEnum.OWNER) {
		// 	throw new BusinessException(ErrorCode.FORBIDDEN_ACCESS, "사장님만 가게를 수정할 수 있습니다.");
		// }

		// 가게 정보 업데이트
		store.update(requestDto);

		return StoreUpdateResponseDto.from(store);
	}

	@Transactional
	public StoreCloseResponseDto closeStore(Long storeId) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));

		// 이미 폐업한 가게인지 확인
		if (store.isClosed()) {
			throw new BusinessException(ErrorCode.INVALID_ACCESS, "이미 폐업한 가게입니다.");
		}

		// 폐업 처리
		store.close();

		return StoreCloseResponseDto.from(store);
	}

	// 가게 목록 조회 메서드 수정 (폐업하지 않은 가게만 조회)
	public ApiResponse<?> getStores(String storeName) {
		// 가게명이 정확히 입력된 경우 -> 상세 조회
		if (storeName != null && !storeName.trim().isEmpty()) {
			Optional<Store> storeOptional = storeRepository.findByStoreNameAndIsClosedFalse(storeName);
			if (storeOptional.isPresent()) {
				return ApiResponse.success(
					"가게 상세 조회 성공",
					StoreDetailResponseDto.from(storeOptional.get())
				);
			}
		}

		// 가게명이 없거나 정확히 일치하는 가게가 없는 경우 -> 목록 조회
		List<Store> stores = (storeName != null && !storeName.trim().isEmpty())
			? storeRepository.findAllByStoreNameAndIsClosedFalse(storeName)
			: storeRepository.findAllByIsClosedFalse();  // 폐업하지 않은 가게만 조회

		return ApiResponse.success(
			"가게 목록 조회 성공",
			StoreListResponseDto.from(stores)
		);
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