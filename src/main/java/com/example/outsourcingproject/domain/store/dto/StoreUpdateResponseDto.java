package com.example.outsourcingproject.domain.store.dto;

import com.example.outsourcingproject.domain.store.entity.Store;

public record StoreUpdateResponseDto(
	Long storeId,
	String storeName,
	String tel,
	String address,
	String openTime,
	String closeTime,
	int minOrderAmount,
	boolean isOperating
) {
	public static StoreUpdateResponseDto from(Store store) {
		return new StoreUpdateResponseDto(
			store.getStoreId(),
			store.getStoreName(),
			store.getTel(),
			store.getAddress(),
			store.getOpenTime(),
			store.getCloseTime(),
			store.getMinOrderAmount(),
			store.isOperating()
		);
	}
}
