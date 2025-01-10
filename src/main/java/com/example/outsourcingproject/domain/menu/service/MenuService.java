package com.example.outsourcingproject.domain.menu.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.outsourcingproject.domain.menu.dto.request.MenuRequestDto;
import com.example.outsourcingproject.domain.menu.dto.response.DeleteMenuResponseDto;
import com.example.outsourcingproject.domain.menu.dto.response.MenuResponseDto;
import com.example.outsourcingproject.domain.menu.entity.Menu;
import com.example.outsourcingproject.domain.menu.repository.MenuRepository;
import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.store.service.StoreService;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.enums.UserRoleEnum;
import com.example.outsourcingproject.exception.auth.UnauthorizedException;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

	private final MenuRepository menuRepository;
	private final StoreService storeService;

	/**
	 * 메뉴 생성
	 */
	@Transactional
	public MenuResponseDto createMenu(Long storeId, MenuRequestDto requestDto, Long userId) {
		Store store = storeService.findStoreById(storeId);
		validateOwnerAccess(userId, store);

		Menu menu = Menu.builder()
			.store(store)
			.menuName(requestDto.menuName())
			.price(requestDto.price())
			.build();

		menuRepository.save(menu);
		return MenuResponseDto.fromEntity(menu);
	}

	/**
	 * 메뉴 수정
	 */
	@Transactional
	public MenuResponseDto updateMenu(Long storeId, Long menuId, MenuRequestDto requestDto, Long userId) {
		Store store = storeService.findStoreById(storeId);
		Menu menu = findMenuById(menuId);
		validateOwnerAccess(userId, store);

		menu.update(requestDto.menuName(), requestDto.price());
		return MenuResponseDto.fromEntity(menu);
	}

	/**
	 * 메뉴 삭제
	 */
	@Transactional
	public DeleteMenuResponseDto deleteMenu(Long storeId, Long menuId, Long userId) {
		Store store = storeService.findStoreById(storeId);
		Menu menu = findMenuById(menuId);
		validateOwnerAccess(userId, store);

		menu.delete();
		return DeleteMenuResponseDto.of(menu);
	}

	private Menu findMenuById(Long menuId) {
		return menuRepository.findById(menuId)
			.orElseThrow(() -> new EntityNotFoundException("메뉴를 찾을 수 없습니다."));
	}

	private void validateOwnerAccess(Long userId, Store store) {
		User owner = store.getOwner();
		if (!owner.getUserId().equals(userId) || owner.getRole() != UserRoleEnum.OWNER) {
			throw new UnauthorizedException("인증이 필요한 접근입니다.");
		}
	}

}
