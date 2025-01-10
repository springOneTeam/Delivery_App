package com.example.outsourcingproject.domain.store.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.outsourcingproject.common.ApiResponse;
import com.example.outsourcingproject.domain.store.dto.StoreCreateRequestWithUserDto;
import com.example.outsourcingproject.domain.store.dto.StoreCreateResponseDto;
import com.example.outsourcingproject.domain.store.dto.StoreUpdateRequestDto;
import com.example.outsourcingproject.domain.store.dto.StoreUpdateResponseDto;
import com.example.outsourcingproject.domain.store.service.StoreService;
import com.example.outsourcingproject.domain.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

	private final StoreService storeService;
	private final UserService userService;

	@PostMapping
	@PreAuthorize("hasRole('OWNER')") // OWNER Role만 접근 가능 - 어노테이션 추가
	public ResponseEntity<ApiResponse<StoreCreateResponseDto>> createStore(
		@AuthenticationPrincipal Long userId, // - 어노테이션 추가
		@Valid @RequestBody StoreCreateRequestWithUserDto requestDto  // - DTO 수정 ( id 제거 )
	) {
		// // User 엔티티로 사용자 조회
		// User user = userService.getUserById(requestDto.userId());  // userId를 DTO에서 가져옴
		//
		// if (user.getRole() != UserRoleEnum.OWNER) {
		// 	throw new UnauthorizedException("사장님만 가게를 등록할 수 있습니다.");
		// }

		// StoreService의 createStore 메서드 호출
		StoreCreateResponseDto responseDto = storeService.createStore(requestDto.toStoreCreateRequestDto(),
			userId); // - 매개변수 userId로 교체

		ApiResponse<StoreCreateResponseDto> response = ApiResponse.success(
			"가게가 성공적으로 등록되었습니다.",
			responseDto
		);

		return ResponseEntity
			.created(URI.create("/api/stores/" + responseDto.storeId()))
			.body(response);
	}

	@PutMapping("/{storeId}")
	public ResponseEntity<ApiResponse<StoreUpdateResponseDto>> updateStore(
		@PathVariable Long storeId,
		@AuthenticationPrincipal Long userId,  // JWT 구현 후 수정 필요
		@Valid @RequestBody StoreUpdateRequestDto requestDto
	) {
		StoreUpdateResponseDto responseDto = storeService.updateStore(storeId, userId, requestDto);

		return ResponseEntity.ok(ApiResponse.success("가게 정보가 성공적으로 수정되었습니다.", responseDto));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<?>> getStores(
		@RequestParam(required = false) String storeName
	) {
		return ResponseEntity.ok(storeService.getStores(storeName));
	}
}
