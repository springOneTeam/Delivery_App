package com.example.outsourcingproject.domain.store.controller;

import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.outsourcingproject.common.ApiResponse;
import com.example.outsourcingproject.domain.store.dto.StoreCloseResponseDto;
import com.example.outsourcingproject.domain.store.dto.StoreCreateRequestDto;
import com.example.outsourcingproject.domain.store.dto.StoreCreateResponseDto;
import com.example.outsourcingproject.domain.store.dto.StoreListResponseDto;
import com.example.outsourcingproject.domain.store.dto.StoreUpdateRequestDto;
import com.example.outsourcingproject.domain.store.dto.StoreUpdateResponseDto;
import com.example.outsourcingproject.domain.store.service.StoreService;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.enums.UserRoleEnum;
import com.example.outsourcingproject.domain.user.service.UserService;
import com.example.outsourcingproject.exception.auth.UnauthorizedException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreControllerTest {

	@InjectMocks
	private StoreController storeController;

	@Mock
	private StoreService storeService;

	@Mock
	private UserService userService;

	/**
	 * 가게 생성 관련 테스트
	 * OWNER 권한 검증 및 정상 생성 프로세스 검증
	 */
	@Nested
	@DisplayName("가게 생성 API 테스트")
	class CreateStore {
		private StoreCreateRequestDto requestDto;
		private User ownerUser;
		private static final Long USER_ID = 1L;

		@BeforeEach
		void setUp() {
			// Given: 테스트에 필요한 기본 데이터 설정
			requestDto = new StoreCreateRequestDto(
				"TestStore",
				"02-1234-5678",
				"Seoul",
				"09:00",
				"22:00",
				10000
			);
			ownerUser = User.create("owner", "owner@test.com", "Password1!", UserRoleEnum.OWNER);
		}

		@Test
		@DisplayName("정상적인 가게 생성 - OWNER 권한")
		void createStore_Success() {
			// Given
			StoreCreateResponseDto responseDto = new StoreCreateResponseDto(
				1L,                  // storeId
				"TestStore",         // storeName
				"02-1234-5678",     // tel
				"Seoul",            // address
				"09:00",           // openTime
				"22:00",           // closeTime
				10000,             // minOrderAmount
				true              // isOperating
			);
			when(userService.getUserById(USER_ID)).thenReturn(ownerUser);
			when(storeService.createStore(any(), any())).thenReturn(responseDto);

			// When
			ResponseEntity<ApiResponse<StoreCreateResponseDto>> response =
				storeController.createStore(USER_ID, requestDto);

			// Then
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
			assertThat(response.getBody().getData()).isEqualTo(responseDto);
			assertThat(response.getHeaders().getLocation()).isEqualTo(URI.create("/api/stores/1"));
			verify(storeService).createStore(any(), any());
		}

		@Test
		@DisplayName("권한 없는 사용자의 가게 생성 시도")
		void createStore_Unauthorized() {
			// Given: USER 권한을 가진 사용자 설정
			User normalUser = User.create("user", "user@test.com", "Password1!", UserRoleEnum.USER);
			when(userService.getUserById(USER_ID)).thenReturn(normalUser);

			// When & Then
			assertThatThrownBy(() -> storeController.createStore(USER_ID, requestDto))
				.isInstanceOf(UnauthorizedException.class)
				.hasMessage("가게 생성은 사장님만 가능합니다.");
		}
	}

	@Nested
	@DisplayName("가게 수정 API 테스트")
	class UpdateStore {
		@Test
		@DisplayName("정상적인 가게 정보 수정")
		void updateStore_Success() {
			// Given - 테스트 데이터 설정
			Long storeId = 1L;
			Long userId = 1L;
			// 업데이트 요청 DTO 생성
			StoreUpdateRequestDto requestDto = new StoreUpdateRequestDto(
				"UpdatedStore",
				"02-8765-4321",
				"Updated Address",
				"10:00",
				"23:00",
				15000,
				true
			);

			// 예상되는 응답 DTO 생성 - 모든 필드 포함
			StoreUpdateResponseDto responseDto = new StoreUpdateResponseDto(
				storeId,
				requestDto.storeName(),
				requestDto.tel(),
				requestDto.address(),
				requestDto.openTime(),
				requestDto.closeTime(),
				requestDto.minOrderAmount(),
				requestDto.isOperating()
			);

			// Mock 서비스 동작 정의
			when(storeService.updateStore(storeId, userId, requestDto))
				.thenReturn(responseDto);

			// When - 컨트롤러 메서드 호출
			ResponseEntity<ApiResponse<StoreUpdateResponseDto>> response =
				storeController.updateStore(storeId, userId, requestDto);

			// Then - 응답 검증
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
			assertThat(response.getBody().getData()).isEqualTo(responseDto);
			verify(storeService).updateStore(storeId, userId, requestDto);
		}
	}
	@Nested
	@DisplayName("가게 조회 API 테스트")
	class GetStores {
		@Test
		@DisplayName("전체 가게 목록 조회")
		void getStores_All() {
			// Given
			List<StoreListResponseDto.StoreDto> storeList = new ArrayList<>();
			storeList.add(new StoreListResponseDto.StoreDto(
				1L, "Store1", "09:00", "22:00", "02-1234-5678", 10000, "Address1"
			));
			storeList.add(new StoreListResponseDto.StoreDto(
				2L, "Store2", "10:00", "23:00", "02-2345-6789", 15000, "Address2"
			));

			StoreListResponseDto listDto = new StoreListResponseDto(storeList);
			ApiResponse<?> expectedResponse = ApiResponse.success(
				"가게 목록 조회 성공",
				listDto
			);

			// 명시적 타입 캐스팅을 사용하여 Mockito의 제네릭 타입 문제 해결
			when(storeService.getStores(any())).thenReturn((ApiResponse) expectedResponse);

			// When
			ResponseEntity<ApiResponse<?>> response = storeController.getStores(null);

			// Then
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
			assertThat(response.getBody()).isEqualTo(expectedResponse);
			StoreListResponseDto resultDto = (StoreListResponseDto) response.getBody().getData();
			assertThat(resultDto.storeList()).hasSize(2);
			assertThat(resultDto.storeList().get(0).storeName()).isEqualTo("Store1");
			assertThat(resultDto.storeList().get(1).storeName()).isEqualTo("Store2");
			verify(storeService).getStores(null);
		}

		@Test
		@DisplayName("가게 이름으로 검색")
		void getStores_ByName() {
			// Given
			String storeName = "TestStore";
			List<StoreListResponseDto.StoreDto> storeList = new ArrayList<>();
			storeList.add(new StoreListResponseDto.StoreDto(
				1L, "TestStore", "09:00", "22:00", "02-1234-5678", 10000, "Address1"
			));

			StoreListResponseDto listDto = new StoreListResponseDto(storeList);
			ApiResponse<?> expectedResponse = ApiResponse.success(
				"가게 목록 조회 성공",
				listDto
			);

			when(storeService.getStores(storeName)).thenReturn((ApiResponse) expectedResponse);

			// When
			ResponseEntity<ApiResponse<?>> response = storeController.getStores(storeName);

			// Then
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
			assertThat(response.getBody()).isEqualTo(expectedResponse);
			StoreListResponseDto resultDto = (StoreListResponseDto) response.getBody().getData();
			assertThat(resultDto.storeList()).hasSize(1);
			assertThat(resultDto.storeList().get(0).storeName()).isEqualTo("TestStore");
			verify(storeService).getStores(storeName);
		}
	}

	@Nested
	@DisplayName("가게 폐업 API 테스트")
	class CloseStore {
		@Test
		@DisplayName("정상적인 가게 폐업 처리")
		void closeStore_Success() {
			// Given
			Long storeId = 1L;
			Long userId = 1L;
			StoreCloseResponseDto responseDto = new StoreCloseResponseDto(
				storeId, "TestStore", false, 0L
			);
			when(storeService.closeStore(storeId, userId)).thenReturn(responseDto);

			// When
			ResponseEntity<ApiResponse<StoreCloseResponseDto>> response =
				storeController.closeStore(storeId, userId);

			// Then
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
			assertThat(response.getBody().getData()).isEqualTo(responseDto);
			verify(storeService).closeStore(storeId, userId);
		}
	}
}