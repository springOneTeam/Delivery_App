package com.example.outsourcingproject.domain.store.dto;

import java.time.LocalTime;

import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.enums.UserRoleEnum;
import com.example.outsourcingproject.exception.ErrorCode;
import com.example.outsourcingproject.exception.common.BusinessException;
import com.fasterxml.jackson.annotation.JsonFormat;

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
	@JsonFormat(pattern = "HH:mm")
	LocalTime openTime,

	@NotNull(message = "마감 시간은 필수입니다")
	@JsonFormat(pattern = "HH:mm")
	LocalTime closeTime,

	@Min(value = 0, message = "최소 주문 금액은 0원 이상이어야 합니다")
	int minOrderAmount
) {
	// 팩토리 메서드
	public static Store toEntity(StoreCreateRequestDto dto, User owner) {
		validateOwnerRole(owner);  // 권한 검증을 팩토리 메서드에서 수행

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

	// 사장님 권한 검증
	private static void validateOwnerRole(User user) {
		if (user.getRole() != UserRoleEnum.OWNER) {
			throw new BusinessException(ErrorCode.FORBIDDEN_ACCESS, "사장님만 가게를 등록할 수 있습니다.");
		}
	}
}