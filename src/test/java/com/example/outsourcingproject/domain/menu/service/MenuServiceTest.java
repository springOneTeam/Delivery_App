package com.example.outsourcingproject.domain.menu.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.outsourcingproject.domain.menu.dto.request.MenuRequestDto;
import com.example.outsourcingproject.domain.menu.dto.response.MenuResponseDto;
import com.example.outsourcingproject.domain.menu.entity.Menu;
import com.example.outsourcingproject.domain.menu.repository.MenuRepository;
import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.store.service.StoreService;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.enums.UserRoleEnum;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	@Mock
	private MenuRepository menuRepository;
	@Mock
	private StoreService storeService;
	@InjectMocks
	private MenuService menuService;

	private static final Long storeId = 1L;
	private static final Long menuId = 1L;
	private static final Long userId = 1L;

	@Test
	@DisplayName("메뉴 생성 성공")
	public void createMenuTest() {
		//given
		MenuRequestDto requestDto = new MenuRequestDto("삼겹살", 10000);
		Store store = mock(Store.class);
		User owner = mock(User.class);

		when(store.getOwner()).thenReturn(owner);
		when(owner.getUserId()).thenReturn(userId);
		when(owner.getRole()).thenReturn(UserRoleEnum.OWNER);
		when(storeService.findStoreById(storeId)).thenReturn(store);

		Menu savedMenu = Menu.builder()
			.store(store)
			.menuName("삼겹살")
			.price(10000)
			.build();
		when(menuRepository.save(any(Menu.class))).thenReturn(savedMenu);

		//when
		MenuResponseDto responseDto = menuService.createMenu(storeId, requestDto, userId);

		//then
		assertThat("삼겹살").isEqualTo(responseDto.menuName());
		assertThat(10000).isEqualTo(responseDto.price());

	}

	@Test
	@DisplayName("메뉴 삭제 성공")
	void deleteMenuTest() {
		//given
		Store store = mock(Store.class);
		User owner = mock(User.class);
		Menu menuToDelete = mock(Menu.class);

		when(store.getOwner()).thenReturn(owner);
		when(owner.getUserId()).thenReturn(userId);
		when(owner.getRole()).thenReturn(UserRoleEnum.OWNER);
		when(storeService.findStoreById(storeId)).thenReturn(store);
		when(menuRepository.findById(menuId)).thenReturn(Optional.of(menuToDelete));

		//when
		menuService.deleteMenu(storeId, menuId, userId);

		//then
		verify(menuRepository, times(1)).findById(menuId);
		verify(menuToDelete, times(1)).delete();
	}


}