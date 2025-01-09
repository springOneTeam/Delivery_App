package com.example.outsourcingproject.domain.store.dto;

import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.user.entity.User;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record StoreCreateRequestDto(
	@NotBlank(message = "상호명은 필수입니다")
	String storeName,

	@NotBlank(message = "전화번호는 필수입니다")
	@Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다")
	String tel,

	@NotBlank(message = "주소는 필수입니다")
	String address,

	@NotNull(message = "오픈 시간은 필수입니다")
	String openTime,

	@NotNull(message = "마감 시간은 필수입니다")
	String closeTime,

	@Min(value = 0, message = "최소 주문 금액은 0원 이상이어야 합니다")
	int minOrderAmount
) {
	public static Store toEntity(StoreCreateRequestDto dto, User owner) {
		return Store.builder()
			.storeName(dto.storeName())
			.tel(dto.tel())
			.address(dto.address())
			.openTime(dto.openTime())
			.closeTime(dto.closeTime())
			.minOrderAmount(dto.minOrderAmount())
			.owner(owner)
			.isOperating(true)
			.build();
	}
}