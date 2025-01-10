package com.example.outsourcingproject.domain.menu.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.outsourcingproject.common.ApiResponse;
import com.example.outsourcingproject.domain.menu.dto.request.MenuRequestDto;
import com.example.outsourcingproject.domain.menu.dto.response.MenuResponseDto;
import com.example.outsourcingproject.domain.menu.service.MenuService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stores/{storeId}/menus")
@RequiredArgsConstructor
public class MenuController {

	private final MenuService menuService;
	// TODO 토큰

	/**
	 * 메뉴 생성 API
	 * 권한: 본인 가게 사장님만 메뉴 생성 가능
	 */
	@PostMapping
	public ResponseEntity<ApiResponse<MenuResponseDto>> createMenu(
		@PathVariable Long storeId,
		@RequestBody MenuRequestDto requestDto,
		@RequestHeader("userId") Long userId
	) {
		// TODO 토큰에서 userId 추출

		MenuResponseDto responseDto = menuService.createMenu(storeId, requestDto, userId);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(ApiResponse.success("메뉴가 생성되었습니다.", responseDto));
	}

	/**
	 * 메뉴 수정 API
	 */
	@PutMapping("/{menuId}")
	public ResponseEntity<ApiResponse<MenuResponseDto>> updateMenu(
		@PathVariable Long storeId,
		@PathVariable Long menuId,
		@RequestBody MenuRequestDto requestDto,
		@RequestHeader("userId") Long userId
	) {
		// TODO 토큰에서 userId 추출

		MenuResponseDto responseDto = menuService.updateMenu(storeId, menuId, requestDto, userId);
		return ResponseEntity.ok(ApiResponse.success("메뉴가 수정되었습니다.", responseDto));
	}

	/**
	 * 메뉴 삭제 API
	 * OWNER가 본인 가게 메뉴만 삭제 가능
	 * SoftDelete
	 */
	@DeleteMapping("/{menuId}")
	public ResponseEntity<ApiResponse<MenuResponseDto>> deleteMenu(
		@PathVariable Long storeId,
		@PathVariable Long menuId,
		@RequestHeader("userId") Long userId
	) {
		// TODO 토큰에서 userId 추출

		menuService.deleteMenu(storeId, menuId, userId);
		return ResponseEntity.ok(ApiResponse.success("메뉴가 삭제되었습니다."));
	}
}
