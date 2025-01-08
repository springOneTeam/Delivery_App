package com.example.outsourcingproject.domain.user.dto.response;

import com.example.outsourcingproject.domain.user.enums.UserRoleEnum;

public record UserSignUpResponseDto(UserRoleEnum role) {

	public static UserSignUpResponseDto toDto(UserRoleEnum role) {
		return new UserSignUpResponseDto(role);
	}

}
