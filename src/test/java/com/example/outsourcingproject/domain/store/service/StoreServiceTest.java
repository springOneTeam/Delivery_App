package com.example.outsourcingproject.domain.store;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.outsourcingproject.domain.store.dto.*;
import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.store.repository.StoreRepository;
import com.example.outsourcingproject.domain.store.service.StoreService;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.repository.UserRepository;
import com.example.outsourcingproject.exception.common.BusinessException;
import com.example.outsourcingproject.domain.user.enums.UserRoleEnum;

import jakarta.persistence.EntityNotFoundException;
/**
 * StoreService 클래스에 대한 단위 테스트
 *
 * 테스트 구조:
 * - @ExtendWith(MockitoExtension.class)를 사용하여 Mockito 확장 기능 활성화
 * - @Nested를 사용하여 관련 테스트들을 논리적으로 그룹화
 * - Given-When-Then 패턴을 사용하여 테스트 구조화
 */
@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

	// 테스트 대상이 되는 서비스. @InjectMocks를 통해 Mock 객체들이 자동 주입됨
	@InjectMocks
	private StoreService storeService;

	// 테스트에서 사용할 Mock 객체들
	@Mock
	private StoreRepository storeRepository;

	@Mock
	private UserRepository userRepository;

	/**
	 * 가게 생성 관련 테스트 그룹
	 * - 정상 생성 케이스
	 * - 최대 가게 수 초과 케이스
	 */
	@Nested
	@DisplayName("가게 생성 테스트")
	class CreateStore {
		private User testUser;
		private StoreCreateRequestDto requestDto;

		@BeforeEach
		void setUp() {
			// 테스트 사용자 생성 및 ID 설정
			// Given (테스트를 위한 사전 조건 설정)
			testUser = User.create("test", "test@test.com", "Password1!", UserRoleEnum.OWNER);
			ReflectionTestUtils.setField(testUser, "userId", 1L);

			// 테스트용 가게 생성 요청 DTO 설정
			requestDto = new StoreCreateRequestDto(
				"TestStore",
				"02-1234-5678",
				"Seoul",
				"09:00",
				"22:00",
				10000
			);
		}

		/**
		 * 정상적인 가게 생성 테스트
		 * 검증 내용:
		 * - 생성된 가게 정보가 요청과 일치하는지
		 * - Repository의 save 메소드가 호출되었는지
		 */
		@Test
		@DisplayName("정상적인 가게 생성")
		void createStore_Success() {
			// Given: 추가적인 테스트 조건 설정
			Store expectedStore = Store.builder()
				.owner(testUser)
				.storeName(requestDto.storeName())
				.tel(requestDto.tel())
				.address(requestDto.address())
				.openTime(requestDto.openTime())
				.closeTime(requestDto.closeTime())
				.minOrderAmount(requestDto.minOrderAmount())
				.isOperating(true)
				.build();

			when(userRepository.findById(any())).thenReturn(Optional.of(testUser));
			//0L은 "현재 사용자가 운영 중인 가게가 없다"는 상태를 의미합니다
			when(storeRepository.countByOwnerAndIsOperatingTrue(any())).thenReturn(0L);
			when(storeRepository.save(any())).thenReturn(expectedStore);

			// When: 실제 테스트할 메서드 호출
			StoreCreateResponseDto response = storeService.createStore(requestDto, 1L);

			// Then: 결과 검증
			assertThat(response).isNotNull();
			assertThat(response.storeName()).isEqualTo(requestDto.storeName());
			verify(storeRepository).save(any());
		}

		@Test
		@DisplayName("최대 가게 수 초과시 실패")
		void createStore_ExceedMaxStores() {
			// Given
			when(userRepository.findById(any())).thenReturn(Optional.of(testUser));
			// 이미 3개의 가게가 있으므로 새로운 가게 생성 실패
			when(storeRepository.countByOwnerAndIsOperatingTrue(any())).thenReturn(3L);

			// When & Then
			assertThatThrownBy(() -> storeService.createStore(requestDto, 1L))
				.isInstanceOf(BusinessException.class);
		}
	}

	/**
	 * 가게 수정 관련 테스트 그룹
	 * - 정상 수정 케이스
	 */
	@Nested
	@DisplayName("가게 수정 테스트")
	class UpdateStore {
		private Store testStore;
		private StoreUpdateRequestDto updateDto;
		private User testUser;

		@BeforeEach
		void setUp() {
			testUser = User.create("test", "test@test.com", "Password1!", UserRoleEnum.OWNER);
			ReflectionTestUtils.setField(testUser, "userId", 1L);

			testStore = Store.builder()
				.owner(testUser)
				.storeName("OldName")
				.tel("02-1234-5678")
				.address("OldAddress")
				.openTime("09:00")
				.closeTime("22:00")
				.minOrderAmount(10000)
				.isOperating(true)
				.build();

			updateDto = new StoreUpdateRequestDto(
				"NewName",
				"02-8765-4321",
				"NewAddress",
				"10:00",
				"23:00",
				15000,
				true
			);
		}

		/**
		 * 가게 폐업 처리 테스트 그룹
		 * - 정상 폐업 처리
		 * - 권한 없는 사용자의 폐업 시도
		 */
		@Test
		@DisplayName("정상적인 가게 정보 수정")
		void updateStore_Success() {
			// Given
			when(storeRepository.findById(any())).thenReturn(Optional.of(testStore));

			// When
			StoreUpdateResponseDto response = storeService.updateStore(1L, 1L, updateDto);

			// Then
			assertThat(response.storeName()).isEqualTo(updateDto.storeName());
			assertThat(response.tel()).isEqualTo(updateDto.tel());
		}
	}

	@Nested
	@DisplayName("가게 폐업 테스트")
	class CloseStore {
		private Store testStore;
		private User testUser;

		@BeforeEach
		void setUp() {
			testUser = User.create("test", "test@test.com", "Password1!", UserRoleEnum.OWNER);
			ReflectionTestUtils.setField(testUser, "userId", 1L);

			testStore = Store.builder()
				.owner(testUser)
				.storeName("TestStore")
				.tel("02-1234-5678")
				.address("TestAddress")
				.openTime("09:00")
				.closeTime("22:00")
				.minOrderAmount(10000)
				.isOperating(true)
				.build();
		}

		@Test
		@DisplayName("정상적인 가게 폐업 처리")
		void closeStore_Success() {
			// Given
			when(storeRepository.findById(any())).thenReturn(Optional.of(testStore));
			when(storeRepository.countByOwnerAndIsOperatingTrue(any())).thenReturn(0L);

			// When
			StoreCloseResponseDto response = storeService.closeStore(1L, 1L);

			// Then
			assertThat(response.isOperating()).isFalse();
			assertThat(response.remainingActiveStores()).isEqualTo(0);
		}

		@Test
		@DisplayName("권한이 없는 사용자의 가게 폐업 시도")
		void closeStore_UnauthorizedUser() {
			// Given
			when(storeRepository.findById(any())).thenReturn(Optional.of(testStore));

			// When & Then
			assertThatThrownBy(() -> storeService.closeStore(1L, 999L))
				.isInstanceOf(BusinessException.class);
		}
	}

	@Nested
	@DisplayName("가게 조회 테스트")
	class GetStores {
		@Test
		@DisplayName("가게 목록 조회 성공")
		void getStores_Success() {
			// Given
			Store store1 = Store.builder()
				.storeName("Store1")
				.tel("02-1234-5678")
				.address("Address1")
				.openTime("09:00")
				.closeTime("22:00")
				.minOrderAmount(10000)
				.isOperating(true)
				.build();

			Store store2 = Store.builder()
				.storeName("Store2")
				.tel("02-2345-6789")
				.address("Address2")
				.openTime("10:00")
				.closeTime("23:00")
				.minOrderAmount(12000)
				.isOperating(true)
				.build();

			when(storeRepository.findAllByIsOperatingTrue())
				.thenReturn(Arrays.asList(store1, store2));

			// When
			var response = storeService.getStores(null);

			// Then
			assertThat(response.isSuccess()).isTrue();
			var storeList = (StoreListResponseDto)response.getData();
			assertThat(storeList.storeList()).hasSize(2);
		}

		@Test
		@DisplayName("특정 가게 이름으로 조회 성공")
		void getStores_ByName_Success() {
			// Given
			Store store = Store.builder()
				.storeName("TestStore")
				.tel("02-1234-5678")
				.address("TestAddress")
				.openTime("09:00")
				.closeTime("22:00")
				.minOrderAmount(10000)
				.isOperating(true)
				.build();

			when(storeRepository.findAllByStoreNameAndIsOperatingTrue("TestStore"))
				.thenReturn(Arrays.asList(store));

			// When
			var response = storeService.getStores("TestStore");

			// Then
			assertThat(response.isSuccess()).isTrue();
			var storeList = (StoreListResponseDto)response.getData();
			assertThat(storeList.storeList()).hasSize(1);
			assertThat(storeList.storeList().get(0).storeName()).isEqualTo("TestStore");
		}
	}
	@Nested
	@DisplayName("가게 단건 조회 테스트")
	class FindStore {
		@Test
		@DisplayName("ID로 가게 조회 성공")
		void findStoreById_Success() {
			// Given
			Store store = Store.builder()
				.storeName("TestStore")
				.tel("02-1234-5678")
				.address("TestAddress")
				.openTime("09:00")
				.closeTime("22:00")
				.minOrderAmount(10000)
				.isOperating(true)
				.build();

			when(storeRepository.findById(1L)).thenReturn(Optional.of(store));

			// When
			Store foundStore = storeService.findStoreById(1L);

			// Then
			assertThat(foundStore).isNotNull();
			assertThat(foundStore.getStoreName()).isEqualTo("TestStore");
		}

		@Test
		@DisplayName("존재하지 않는 ID로 조회시 예외 발생")
		void findStoreById_NotFound() {
			// Given
			when(storeRepository.findById(any())).thenReturn(Optional.empty());

			// When & Then
			assertThatThrownBy(() -> storeService.findStoreById(1L))
				.isInstanceOf(EntityNotFoundException.class);
		}
	}

	@Nested
	@DisplayName("가게 목록 조회 추가 테스트")
	class GetStoresAdditional {
		@Test
		@DisplayName("검색어가 빈 문자열일 때 전체 목록 조회")
		void getStores_EmptySearchTerm() {
			// Given
			when(storeRepository.findAllByIsOperatingTrue())
				.thenReturn(Arrays.asList(new Store(), new Store()));

			// When
			var response = storeService.getStores("");

			// Then
			assertThat(response.isSuccess()).isTrue();
			var storeList = (StoreListResponseDto)response.getData();
			assertThat(storeList.storeList()).hasSize(2);
		}

		@Test
		@DisplayName("검색어로 조회시 결과 없음")
		void getStores_NoResults() {
			// Given
			when(storeRepository.findAllByStoreNameAndIsOperatingTrue("NonExistent"))
				.thenReturn(Arrays.asList());

			// When
			var response = storeService.getStores("NonExistent");

			// Then
			assertThat(response.isSuccess()).isTrue();
			var storeList = (StoreListResponseDto)response.getData();
			assertThat(storeList.storeList()).isEmpty();
		}
	}

	@Nested
	@DisplayName("가게 조회 테스트")
	class GetStore {
		@Test
		@DisplayName("가게 목록 조회 성공")
		void getStores_Success() {
			// Given
			Store store1 = Store.builder()
				.storeName("Store1")
				.tel("02-1234-5678")
				.address("Address1")
				.openTime("09:00")
				.closeTime("22:00")
				.minOrderAmount(10000)
				.isOperating(true)
				.build();

			Store store2 = Store.builder()
				.storeName("Store2")
				.tel("02-2345-6789")
				.address("Address2")
				.openTime("10:00")
				.closeTime("23:00")
				.minOrderAmount(12000)
				.isOperating(true)
				.build();

			when(storeRepository.findAllByIsOperatingTrue())
				.thenReturn(Arrays.asList(store1, store2));

			// When
			var response = storeService.getStores(null);

			// Then
			assertThat(response.isSuccess()).isTrue();
			var storeList = (StoreListResponseDto)response.getData();
			assertThat(storeList.storeList()).hasSize(2);
		}

		@Test
		@DisplayName("특정 가게 이름으로 조회 성공")
		void getStores_ByName_Success() {
			// Given
			Store store = Store.builder()
				.storeName("TestStore")
				.tel("02-1234-5678")
				.address("TestAddress")
				.openTime("09:00")
				.closeTime("22:00")
				.minOrderAmount(10000)
				.isOperating(true)
				.build();

			when(storeRepository.findAllByStoreNameAndIsOperatingTrue("TestStore"))
				.thenReturn(Arrays.asList(store));

			// When
			var response = storeService.getStores("TestStore");

			// Then
			assertThat(response.isSuccess()).isTrue();
			var storeList = (StoreListResponseDto)response.getData();
			assertThat(storeList.storeList()).hasSize(1);
			assertThat(storeList.storeList().get(0).storeName()).isEqualTo("TestStore");
		}
	}
}