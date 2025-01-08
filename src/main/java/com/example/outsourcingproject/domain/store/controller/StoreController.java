package com.example.outsourcingproject.domain.store.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.outsourcingproject.common.ApiResponse;
import com.example.outsourcingproject.domain.store.dto.StoreCreateRequestDto;
import com.example.outsourcingproject.domain.store.dto.StoreCreateResponseDto;
import com.example.outsourcingproject.domain.store.service.StoreService;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.enums.UserRoleEnum;
import com.example.outsourcingproject.domain.user.service.UserService;
import com.example.outsourcingproject.exception.auth.UnauthorizedException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

	private final StoreService storeService;
	private final UserService userService;

	@PostMapping
	public ResponseEntity<ApiResponse<StoreCreateResponseDto>> createStore(
		@AuthenticationPrincipal Long userId,
		@Valid @RequestBody StoreCreateRequestDto requestDto
	) {
		// User 엔티티로 사용자 조회
		User user = userService.getUserById(userId);  // findById -> getUserById로 메서드명 변경

		if (user.getRole() != UserRoleEnum.OWNER) {
			throw new UnauthorizedException("사장님만 가게를 등록할 수 있습니다.");
		}

		// StoreService의 createStore 메서드 시그니처와 일치하도록 수정
		StoreCreateResponseDto responseDto = storeService.createStore(requestDto, userId);

		ApiResponse<StoreCreateResponseDto> response = ApiResponse.success(
			"가게가 성공적으로 등록되었습니다.",
			responseDto
		);

		return ResponseEntity
			.created(URI.create("/api/stores/" + responseDto.storeId()))
			.body(response);
	}
}
