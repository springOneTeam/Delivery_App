package com.example.outsourcingproject.domain.order.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangeOrderStatusRequestDto(
	@NotBlank(message = "주문 상태 입력은 필수입니다.")
	String orderStatus
) {
}
