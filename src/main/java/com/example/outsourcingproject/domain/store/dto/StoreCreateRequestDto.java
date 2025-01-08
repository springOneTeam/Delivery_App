package com.example.outsourcingproject.domain.store.dto;

import java.time.LocalTime;

import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.user.entity.User;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record StoreCreateRequestDto(
	@NotBlank(message = "상호명은 필수입니다.")
	String storeName,

	@NotBlank(message = "전화번호는 필수입니다.")
	@Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
	String tel,

	@NotBlank(message = "주소는 필수입니다.")
	String address,

	@NotNull(message = "오픈 시간은 필수입니다.")
	LocalTime openTime,

	@NotNull(message = "마감 시간은 필수입니다.")
	LocalTime closeTime,

	@Min(value = 0, message = "최소 주문 금액은 0원 이상이어야 합니다.")
	int minOrderAmount
) {
	// 컴팩트 생성자를 사용한 시간 검증
	public StoreCreateRequestDto {
		if (openTime != null && closeTime != null && openTime.isAfter(closeTime)) {
			throw new IllegalArgumentException("오픈 시간이 마감 시간보다 늦을 수 없습니다.");
		}
	}

	public static StoreCreateRequestDto of(
		String storeName,
		String tel,
		String address,
		LocalTime openTime,
		LocalTime closeTime,
		int minOrderAmount
	) {
		return new StoreCreateRequestDto(
			storeName,
			tel,
			address,
			openTime,
			closeTime,
			minOrderAmount
		);
	}

	public Store toEntity(User owner) {
		return Store.builder()
			.owner(owner)
			.storeName(storeName)
			.tel(tel)
			.address(address)
			.openTime(openTime)
			.closeTime(closeTime)
			.minOrderAmount(minOrderAmount)
			.isOperating(true)
			.build();
	}
}
