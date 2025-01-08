package com.example.outsourcingproject.domain.user.dto.request;

import com.example.outsourcingproject.domain.user.enums.UserRoleEnum;

public record UserSignUpRequestDto(String nickName, String email, String password, UserRoleEnum role) {
}
