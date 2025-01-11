package com.example.outsourcingproject.domain.user.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.outsourcingproject.common.ApiResponse;
import com.example.outsourcingproject.domain.user.dto.request.UserDeleteRequestDto;
import com.example.outsourcingproject.domain.user.dto.request.UserLoginRequesetDto;
import com.example.outsourcingproject.domain.user.dto.request.UserSignUpRequestDto;
import com.example.outsourcingproject.domain.user.dto.response.UserLoginResponseDto;
import com.example.outsourcingproject.domain.user.dto.response.UserSignUpResponseDto;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	// 회원가입
	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<UserSignUpResponseDto>> singUpUser(
		@Valid @RequestBody UserSignUpRequestDto requestDto) {

		UserSignUpResponseDto response = userService.signUpUser(requestDto);
		return ResponseEntity
			.created(URI.create("/api/stores/" + response))
			.body(ApiResponse.success("signup user", response));
	}

	// 로그인
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<UserLoginResponseDto>> loginUser(
		@RequestBody UserLoginRequesetDto requestDto) {

		User.validateEmail(requestDto.email());
		UserLoginResponseDto response = userService.loginUser(requestDto);
		return ResponseEntity.ok(ApiResponse.success("login success", response));
	}

	// 회원탈퇴
	@DeleteMapping
	public ResponseEntity<ApiResponse> deleteUser(
		@RequestBody UserDeleteRequestDto requestDto,
		@AuthenticationPrincipal Long userId) {

		userService.deleteUser(requestDto, userId);
		return ResponseEntity.ok(ApiResponse.success("user is deleted", null));
	}

}
