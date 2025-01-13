package com.example.outsourcingproject.domain.store.dto;

import com.example.outsourcingproject.domain.store.entity.Store;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
class StoreDtoTest {

	@Test
	@DisplayName("Store 엔티티로부터 StoreDetailResponseDto 생성")
	void from_StoreDetailResponseDto() {
		// Given
		Store store = Store.builder()
			.storeName("TestStore")
			.openTime("09:00")
			.closeTime("22:00")
			.tel("02-1234-5678")
			.minOrderAmount(10000)
			.address("Address1")
			.isOperating(true)
			.build();
		// ID는 JPA가 자동으로 설정하므로, 리플렉션을 통해 테스트용 ID 설정
		setStoreId(store, 1L);

		// When
		StoreDetailResponseDto dto = StoreDetailResponseDto.from(store);

		// Then
		assertThat(dto.storeName()).isEqualTo(store.getStoreName());
		assertThat(dto.openTime()).isEqualTo(store.getOpenTime());
		assertThat(dto.closeTime()).isEqualTo(store.getCloseTime());
		assertThat(dto.tel()).isEqualTo(store.getTel());
		assertThat(dto.minOrderAmount()).isEqualTo(store.getMinOrderAmount());
		assertThat(dto.address()).isEqualTo(store.getAddress());
		assertThat(dto.menus()).isEmpty();
	}

	@Test
	@DisplayName("Store 엔티티로부터 StoreResponseDto 생성")
	void from_StoreResponseDto() {
		// Given
		Store store = Store.builder()
			.storeName("TestStore")
			.openTime("09:00")
			.closeTime("22:00")
			.tel("02-1234-5678")
			.minOrderAmount(10000)
			.address("Address1")
			.isOperating(true)
			.build();
		setStoreId(store, 1L);

		// When
		StoreResponseDto dto = StoreResponseDto.from(store);

		// Then
		assertThat(dto.storeName()).isEqualTo(store.getStoreName());
		assertThat(dto.openTime()).isEqualTo(store.getOpenTime());
		assertThat(dto.closeTime()).isEqualTo(store.getCloseTime());
		assertThat(dto.tel()).isEqualTo(store.getTel());
		assertThat(dto.minOrderAmount()).isEqualTo(store.getMinOrderAmount());
		assertThat(dto.address()).isEqualTo(store.getAddress());
	}

	private void setStoreId(Store store, Long id) {
		try {
			var field = Store.class.getDeclaredField("storeId");
			field.setAccessible(true);
			field.set(store, id);
		} catch (Exception e) {
			throw new RuntimeException("Failed to set store ID", e);
		}
	}
}