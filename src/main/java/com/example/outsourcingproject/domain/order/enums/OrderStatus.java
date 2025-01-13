package com.example.outsourcingproject.domain.order.enums;

import com.example.outsourcingproject.exception.ErrorCode;
import com.example.outsourcingproject.exception.common.BusinessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum OrderStatus {
	PENDING, //보류 상태(주문 요청 완료)
	ACCEPTED, //주문 수락
	REJECTED, // 주문 거절
	COOKING, // 조리 시작
	DELIVERING, // 배달 시작
	DELIVERED; // 배달 완료

	@JsonCreator
	public static OrderStatus from(String value) {
		try{
			return OrderStatus.valueOf(value.toUpperCase());
		} catch (IllegalArgumentException e){
			throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "입력한 값에 오타가 없는지 확인해주세요.");
		}
	}
}
