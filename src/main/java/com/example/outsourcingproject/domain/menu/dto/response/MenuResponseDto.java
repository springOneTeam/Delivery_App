package com.example.outsourcingproject.domain.menu.dto.response;

import com.example.outsourcingproject.domain.menu.entity.Menu;

public record MenuResponseDto(Long menuId, String menuName, int price) {

	public static MenuResponseDto fromEntity(Menu menu) {
		return new MenuResponseDto(menu.getMenuId(), menu.getMenuName(), menu.getPrice());
	}

}
