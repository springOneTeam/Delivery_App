package com.example.outsourcingproject.domain.store.dto;

import com.example.outsourcingproject.domain.store.entity.Store;

public record StoreResponseDto(
	Long storeId,
	String storeName,
	String openTime,
	String closeTime,
	String tel,
	int minOrderAmount,
	String address
) {
	public static StoreResponseDto from(Store store) {
		return new StoreResponseDto(
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
