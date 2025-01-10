package com.example.outsourcingproject.domain.store.dto;

import com.example.outsourcingproject.domain.store.entity.Store;

public record StoreCloseResponseDto(
	Long storeId,
	String storeName,
	boolean isOperating,
	long remainingActiveStores
) {
	public static StoreCloseResponseDto from(Store store, long remainingActiveStores) {
		return new StoreCloseResponseDto(
			store.getStoreId(),
			store.getStoreName(),
			store.isOperating(),
			remainingActiveStores
		);
	}
}