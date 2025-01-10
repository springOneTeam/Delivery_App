package com.example.outsourcingproject.domain.user.dto.response;

public record UserLoginResponseDto(String accessToken) {

	public static UserLoginResponseDto toDto(String accessToken) {
		return new UserLoginResponseDto(accessToken);
	}
}
