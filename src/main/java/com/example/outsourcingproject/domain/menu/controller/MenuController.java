package com.example.outsourcingproject.domain.menu.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	 */
	@PostMapping
	public ResponseEntity<MenuResponseDto> createMenu(
		@PathVariable Long storeId,
		@RequestBody MenuRequestDto requestDto,
		@RequestHeader("Authorization") String token
	) {
		// TODO 토큰에서 userId 추출

		MenuResponseDto responseDto = menuService.createMenu(storeId, requestDto, userId);
		return ResponseEntity.ok(responseDto);
	}

	/**
	 * 메뉴 수정 API
	 */
	@PutMapping("/{menuId}")
	public ResponseEntity<MenuResponseDto> updateMenu(
		@PathVariable Long storeId,
		@PathVariable Long menuId,
		@RequestBody MenuRequestDto requestDto,
		@RequestHeader("Authorization") String token
	) {
		// TODO 토큰에서 userId 추출

		MenuResponseDto responseDto = menuService.updateMenu(storeId, menuId, requestDto, userId);
		return ResponseEntity.ok(responseDto);
	}

	/**
	 * 메뉴 삭제 API
	 */
	@DeleteMapping("/{menuId}")
	public ResponseEntity<Void> deleteMenu(
		@PathVariable Long storeId,
		@PathVariable Long menuId,
		@RequestHeader("Authorization") String token
	) {
		// TODO 토큰에서 userId 추출

		menuService.deleteMenu(storeId, menuId, userId);
		return ResponseEntity.noContent().build();
	}
}
