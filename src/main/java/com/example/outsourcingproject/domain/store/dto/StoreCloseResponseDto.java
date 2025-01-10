package com.example.outsourcingproject.domain.store.dto;

import com.example.outsourcingproject.domain.store.entity.Store;

public record StoreCloseResponseDto(
	Long storeId,
	String storeName,
	boolean isClosed
) {
	public static StoreCloseResponseDto from(Store store) {
		return new StoreCloseResponseDto(
			store.getStoreId(),
			store.getStoreName(),
			store.isClosed()
		);
	}
}