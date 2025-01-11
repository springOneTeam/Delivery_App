package com.example.outsourcingproject.domain.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.outsourcingproject.common.ApiResponse;
import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.store.repository.StoreRepository;
import com.example.outsourcingproject.domain.user.dto.request.UserDeleteRequestDto;
import com.example.outsourcingproject.domain.user.dto.request.UserLoginRequesetDto;
import com.example.outsourcingproject.domain.user.dto.request.UserSignUpRequestDto;
import com.example.outsourcingproject.domain.user.dto.response.UserLoginResponseDto;
import com.example.outsourcingproject.domain.user.dto.response.UserSignUpResponseDto;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.repository.UserRepository;
import com.example.outsourcingproject.exception.ErrorCode;
import com.example.outsourcingproject.exception.common.BusinessException;
import com.example.outsourcingproject.exception.common.UserNotFoundException;
import com.example.outsourcingproject.utils.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final StoreRepository storeRepository;
	private final JwtUtil jwtUtil;

	/**
	 * 회원가입 기능
	 */
	@Transactional
	public UserSignUpResponseDto signUpUser(UserSignUpRequestDto requestDto) {
		// 비즈니스 규칙: 이메일 검증
		verifyEmail(requestDto.email());
		// 비즈니스 규칙: 비밀번호 형식 검증
		User.validatePassword(requestDto.password());
		// 비밀번호 암호화
		String encryptedPassword = User.generateEncryptedPassword(requestDto.password());

		User newUser = User.create(requestDto.nickName(), requestDto.email(), encryptedPassword, requestDto.role());
		User savedUser = userRepository.save(newUser);
		return UserSignUpResponseDto.toDto(savedUser.getRole());
	}

	/**
	 * 로그인 기능
	 */
	@Transactional
	public UserLoginResponseDto loginUser(UserLoginRequesetDto requestDto) {
		User foundUser = userRepository.findByEmail(requestDto.email());
		isDeletedCheck(foundUser);
		matchPassword(requestDto.password(), foundUser.getPassword());
		// 토큰 생성
		String generatedToken = jwtUtil.generateToken(foundUser.getUserId(), foundUser.getRole().toString());
		return UserLoginResponseDto.toDto(generatedToken, foundUser.getRole());
	}


	/**
	 * 회원 삭제 기능
	 */
	@Transactional
	public void deleteUser(UserDeleteRequestDto requestDto, Long userId) {
		User foundUser = getUserById(userId);
		matchPassword(requestDto.password(), foundUser.getPassword());
		isDeletedCheck(foundUser);
	}


	//------------------- public Methods -------------------
	public User getUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
	}

	//------------------- private Methods -------------------
	private void verifyEmail(String Email) {
		User.generateEmail(Email);
		if (userRepository.existsByEmail(Email)) {
			throw new BusinessException(ErrorCode.DUPLICATE_EMAIL, "다른 이메일로 재시도하시기 바랍니다.");
		}
	}

	private void matchPassword(String requestPassword, String storagePassword) {
		User.matchesPassword(requestPassword, storagePassword);
	}

	private void isDeletedCheck(User foundUser) {
		if(foundUser.getIsDeleted()) {
			throw new BusinessException(ErrorCode.DUPLICATE_DELETED,"계정이 비활성화 상태입니다.");
		}
	}

}
