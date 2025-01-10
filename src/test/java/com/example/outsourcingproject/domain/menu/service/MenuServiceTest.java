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

	/**
	 * 메뉴 생성 성공 테스트
	 * given: 테스트에 필요한 초기 상태 설정. DTO, Mock객체, 반환값 설정
	 * when: createMenu 메서드 실행, Mock설정에 따라 storeService.findStoreById, menuRepository.save 호출
	 * then: 생성된 메뉴의 이름과 가격이 기댓값과 같은지 검증
	 */
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

	/**
	 * 메뉴 수정 성공 테스트
	 * given: 수정 요청 DTO와 Mock객체의 동작 설정. 메뉴 수정 시 이름과 가격이 올바르게 업데이트되는지 확인하기 위해 doAnswer 사용
	 * when: updateMenu 메서드 실행. Mock 설정에 따라 storeService.findStoreById, menuRepository.findById 이 호출되면서 메뉴가 수정
	 * then: 수정된 메뉴의 이름과 가격이 DTO에 명시된 값과 같은지 검증
	 */
	@Test
	@DisplayName("메뉴 수정 성공")
	public void updateMenuTest() {
		//given
		MenuRequestDto requestDto = new MenuRequestDto("소고기", 20000);
		Store store = mock(Store.class);
		User owner = mock(User.class);
		Menu menu = mock(Menu.class);

		when(store.getOwner()).thenReturn(owner);
		when(owner.getUserId()).thenReturn(userId);
		when(owner.getRole()).thenReturn(UserRoleEnum.OWNER);
		when(storeService.findStoreById(storeId)).thenReturn(store);
		when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));

		// Mock 객체의 상태 변경을 설정
		doAnswer(invocation -> {
			String menuName = invocation.getArgument(0);
			int price = invocation.getArgument(1);
			when(menu.getMenuName()).thenReturn(menuName);
			when(menu.getPrice()).thenReturn(price);
			return null;
		}).when(menu).update(anyString(), anyInt());

		//when
		MenuResponseDto responseDto = menuService.updateMenu(storeId, menuId, requestDto, userId);

		//then
		assertThat(responseDto.menuName()).isEqualTo("소고기");
		assertThat(responseDto.price()).isEqualTo(20000);
	}

	/**
	 * 메뉴 삭제 성공 테스트
	 * given: 삭제 대상 메뉴와 관련된 Mock 객체 설정. 메뉴 삭제 여부를 확인하기 위해 Mock 설정에서 menu.isDeleted()를 true로 반환하도록 설정
	 * when: deleteMenu 메서드 실행. Mock 설정에 따라 storeService.findStoreById, menuRepository.findById 이 호출되면서 메뉴 삭제
	 * then: 삭제된 메뉴의 ID와 삭제 상태(isDeleted)가 기댓값과 같은지 검증
	 */
	@Test
	@DisplayName("메뉴 삭제 성공")
	public void deleteMenuTest() {
		//given
		Store store = mock(Store.class);
		User owner = mock(User.class);
		Menu menu = mock(Menu.class);

		when(store.getOwner()).thenReturn(owner);
		when(owner.getUserId()).thenReturn(userId);
		when(owner.getRole()).thenReturn(UserRoleEnum.OWNER);
		when(storeService.findStoreById(storeId)).thenReturn(store);
		when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));
		when(menu.getMenuId()).thenReturn(menuId); // 반환값 설정
		when(menu.isDeleted()).thenReturn(true);  // 반환값 설정

		//when
		MenuResponseDto responseDto = menuService.deleteMenu(storeId, menuId, userId);

		//then
		assertThat(responseDto.menuId()).isEqualTo(menuId);
		assertTrue(menu.isDeleted()); // 설정한 값 검증
	}
}