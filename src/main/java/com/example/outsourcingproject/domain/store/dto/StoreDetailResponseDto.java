package com.example.outsourcingproject.domain.store.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.example.outsourcingproject.domain.menu.dto.response.MenuResponseDto;
import com.example.outsourcingproject.domain.store.entity.Store;

public record StoreDetailResponseDto(
	Long storeId,
	String storeName,
	String openTime,
	String closeTime,
	String tel,
	int minOrderAmount,
	String address,
	List<MenuResponseDto> menus
) {
	public static StoreDetailResponseDto from(Store store) {
		List<MenuResponseDto> menuList = store.getMenus().stream()
			.map(MenuResponseDto::fromEntity)
			.collect(Collectors.toList());

		return new StoreDetailResponseDto(
			store.getStoreId(),
			store.getStoreName(),
			store.getOpenTime(),
			store.getCloseTime(),
			store.getTel(),
			store.getMinOrderAmount(),
			store.getAddress(),
			menuList
		);
	}
}