package com.example.outsourcingproject.domain.store.dto;
import java.time.LocalTime;

import com.example.outsourcingproject.domain.store.entity.Store;

public record StoreCreateResponseDto(
	Long storeId,
	String storeName,
	String tel,
	String address,
	String openTime,
	String closeTime,
	int minOrderAmount,
	boolean isOperating
) {
	public static StoreCreateResponseDto from(Store store) {
		return new StoreCreateResponseDto(
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