package com.example.outsourcingproject.domain.store.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.example.outsourcingproject.domain.store.entity.Store;

public record StoreListResponseDto(
	List<StoreDto> storeList  // 변경된 부분
) {
	public static StoreListResponseDto from(List<Store> stores) {
		List<StoreDto> storeList = stores.stream()
			.map(StoreDto::from)
			.collect(Collectors.toList());
		return new StoreListResponseDto(storeList);
	}

	// 내부 DTO 추가
	public record StoreDto(
		Long storeId,
		String storeName,
		String openTime,
		String closeTime,
		String tel,
		int minOrderAmount,
		String address
	) {
		public static StoreDto from(Store store) {
			return new StoreDto(
				store.getStoreId(),
				store.getStoreName(),
				store.getOpenTime(),
				store.getCloseTime(),
				store.getTel(),
				store.getMinOrderAmount(),
				store.getAddress()
			);
		}
	}
}