package com.example.outsourcingproject.domain.user.service;

import org.springframework.stereotype.Service;

import com.example.outsourcingproject.domain.user.dto.request.UserSignUpRequestDto;
import com.example.outsourcingproject.domain.user.dto.response.UserSignUpResponseDto;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	/**
	 * TODO :: 추후 토큰 생성 후 반환
	 * @param requestDto
	 * @return
	 */
	public UserSignUpResponseDto signUpUser(UserSignUpRequestDto requestDto) {

		// 비즈니스 규칙: 이메일은 정복되어선 안된다.
		verifyEmail(requestDto.email());

		// 비밀번호 형식 검증
		User.validatePassword(requestDto.password());

		String encryptedPassword = User.generateEncryptedPassword(requestDto.password()).getPassword();

		User newUser = User.create(requestDto.nickName(), requestDto.email(), encryptedPassword, requestDto.role());

		User savedUser = userRepository.save(newUser);

		return UserSignUpResponseDto.toDto(savedUser.getRole());
	}


	//------------------- private common Methods -------------------
	private void verifyEmail(String Email) {
		User.generateEmail(Email);
	}

}
