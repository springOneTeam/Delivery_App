package com.example.outsourcingproject.domain.order.enums;

public enum OrderStatus {
	PENDING, //보류 상태(주문 요청 완료)
	ACCEPTED, //주문 수락
	REJECTED, // 주문 거절
	COOKING, // 조리 시작
	DELIVERING, // 배달 시작
	DELIVERED // 배달 완료
}
