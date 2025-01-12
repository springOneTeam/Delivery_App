package com.example.outsourcingproject.domain.menu.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.outsourcingproject.common.ApiResponse;
import com.example.outsourcingproject.domain.menu.dto.request.MenuRequestDto;
import com.example.outsourcingproject.domain.menu.dto.response.MenuResponseDto;
import com.example.outsourcingproject.domain.menu.service.MenuService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stores/{storeId}/menus")
@RequiredArgsConstructor
public class MenuController {

	private final MenuService menuService;

	@PostMapping
	@PreAuthorize("hasRole('OWNER')")
	@Operation(summary = "메뉴 생성", description = "사장님(OWNER) 권한을 가진 사용자만 본인 가게 메뉴를 생성할 수 있습니다.")
	public ResponseEntity<ApiResponse<MenuResponseDto>> createMenu(
		@PathVariable Long storeId,
		@RequestBody MenuRequestDto requestDto,
		@AuthenticationPrincipal Long userId
	) {
		MenuResponseDto responseDto = menuService.createMenu(storeId, requestDto, userId);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(ApiResponse.success("메뉴가 생성되었습니다.", responseDto));
	}

	@PutMapping("/{menuId}")
	@PreAuthorize("hasRole('OWNER')")
	@Operation(summary = "메뉴 수정", description = "사장님(OWNER) 권한을 가진 사용자만 본인 가게 메뉴를 수정할 수 있습니다.")
	public ResponseEntity<ApiResponse<MenuResponseDto>> updateMenu(
		@PathVariable Long storeId,
		@PathVariable Long menuId,
		@RequestBody MenuRequestDto requestDto,
		@AuthenticationPrincipal Long userId
	) {
		MenuResponseDto responseDto = menuService.updateMenu(storeId, menuId, requestDto, userId);
		return ResponseEntity.ok(ApiResponse.success("메뉴가 수정되었습니다.", responseDto));
	}

	@DeleteMapping("/{menuId}")
	@PreAuthorize("hasRole('OWNER')")
	@Operation(summary = "메뉴 삭제", description = "사장님(OWNER) 권한을 가진 사용자만 본인 가게 메뉴를 삭제할 수 있습니다.")
	public ResponseEntity<ApiResponse<MenuResponseDto>> deleteMenu(
		@PathVariable Long storeId,
		@PathVariable Long menuId,
		@AuthenticationPrincipal Long userId
	) {
		MenuResponseDto responseDto = menuService.deleteMenu(storeId, menuId, userId);
		return ResponseEntity.ok(ApiResponse.success("메뉴가 삭제되었습니다.", responseDto));
	}
}
