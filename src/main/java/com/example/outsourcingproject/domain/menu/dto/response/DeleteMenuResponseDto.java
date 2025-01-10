package com.example.outsourcingproject.domain.menu.dto.response;

import com.example.outsourcingproject.domain.menu.entity.Menu;

public record DeleteMenuResponseDto(Long menuId, String message) {
	public static DeleteMenuResponseDto of(Menu menu) {
		return new DeleteMenuResponseDto(menu.getMenuId(), "메뉴가 삭제되었습니다.");
	}
}
