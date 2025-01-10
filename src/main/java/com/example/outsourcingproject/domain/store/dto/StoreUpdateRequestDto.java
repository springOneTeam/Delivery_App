package com.example.outsourcingproject.domain.store.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record StoreUpdateRequestDto(
	@NotBlank(message = "상호명은 필수입니다")
	String storeName,

	@NotBlank(message = "전화번호는 필수입니다")
	@Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다")
	String tel,

	@NotBlank(message = "주소는 필수입니다")
	String address,

	@NotBlank(message = "오픈 시간은 필수입니다")
	@Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "시간 형식이 올바르지 않습니다 (HH:mm)")
	String openTime,

	@NotBlank(message = "마감 시간은 필수입니다")
	@Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "시간 형식이 올바르지 않습니다 (HH:mm)")
	String closeTime,

	@Min(value = 0, message = "최소 주문 금액은 0원 이상이어야 합니다")
	int minOrderAmount,

	boolean isOperating
) {
}

