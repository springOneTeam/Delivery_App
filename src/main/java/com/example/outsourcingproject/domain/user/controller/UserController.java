package com.example.outsourcingproject.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.outsourcingproject.common.ApiResponse;
import com.example.outsourcingproject.domain.user.dto.request.UserLoginRequesetDto;
import com.example.outsourcingproject.domain.user.dto.request.UserSignUpRequestDto;
import com.example.outsourcingproject.domain.user.dto.response.UserLoginResponseDto;
import com.example.outsourcingproject.domain.user.dto.response.UserSignUpResponseDto;
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
		ApiResponse apiResponse = ApiResponse.success("created", response);
		return new ResponseEntity<ApiResponse<UserSignUpResponseDto>>(apiResponse, HttpStatus.CREATED);
	}

	// 로그인
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<UserLoginResponseDto>> loginUser(@RequestBody UserLoginRequesetDto requestDto) {
		UserLoginResponseDto response = userService.loginUser(requestDto);
		ApiResponse apiResponse = ApiResponse.success("login", response);
		return new ResponseEntity<ApiResponse<UserLoginResponseDto>>(apiResponse, HttpStatus.OK);
	}

}
