package com.example.outsourcingproject.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.outsourcingproject.domain.user.dto.request.UserLoginRequesetDto;
import com.example.outsourcingproject.domain.user.dto.request.UserSignUpRequestDto;
import com.example.outsourcingproject.domain.user.dto.response.UserLoginResponseDto;
import com.example.outsourcingproject.domain.user.dto.response.UserSignUpResponseDto;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.repository.UserRepository;
import com.example.outsourcingproject.exception.ErrorCode;
import com.example.outsourcingproject.exception.common.BusinessException;
import com.example.outsourcingproject.utils.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	/**
	 * TODO :: 추후 토큰 생성 후 반환
	 * @param requestDto
	 * @return
	 */
	public UserSignUpResponseDto signUpUser(UserSignUpRequestDto requestDto) {
		// 비즈니스 규칙: 이메일 검증
		verifyEmail(requestDto.email());
		// 비즈니스 규칙: 비밀번호 형식 검증
		User.validatePassword(requestDto.password());
		// 비밀번호 암호화
		String encryptedPassword = User.generateEncryptedPassword(requestDto.password());

		// 새 유저 생성
		User newUser = User.create(requestDto.nickName(), requestDto.email(), encryptedPassword, requestDto.role());
		User savedUser = userRepository.save(newUser);
		return UserSignUpResponseDto.toDto(savedUser.getRole());
	}


	public UserLoginResponseDto loginUser(UserLoginRequesetDto requestDto) {
		User foundUser = userRepository.findByEmail(requestDto.email());

		// 비즈니스 규칙: 이메일이 중복되어서는 안된다.
		matchPassword(requestDto.password(), foundUser.getPassword());
		String generatedToken = jwtUtil.generateToken(foundUser.getUserId(), foundUser.getRole().toString());
		return UserLoginResponseDto.toDto(generatedToken);
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
			throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
		}
	}

	private void matchPassword(String requestPassword, String storagePassword) {
		User.matchesPassword(requestPassword, storagePassword);
	}

}
