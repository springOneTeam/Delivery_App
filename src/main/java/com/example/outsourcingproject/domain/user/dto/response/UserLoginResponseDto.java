package com.example.outsourcingproject.domain.user.dto.response;

import com.example.outsourcingproject.domain.user.enums.UserRoleEnum;

public record UserLoginResponseDto(String accessToken, UserRoleEnum role) {

	public static UserLoginResponseDto toDto(String accessToken, UserRoleEnum role) {
		return new UserLoginResponseDto(accessToken, role);
	}
}
