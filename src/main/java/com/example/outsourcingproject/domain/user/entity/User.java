package com.example.outsourcingproject.domain.user.entity;

import java.util.regex.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.outsourcingproject.domain.user.enums.UserRoleEnum;
import com.example.outsourcingproject.utils.BCrypUtil;
import com.example.outsourcingproject.exception.ErrorCode;
import com.example.outsourcingproject.exception.common.BusinessException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Table(name = "users")
@Entity
public class User {

	private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
	private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
	private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
	private static final Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Column(nullable = false)
	private String nickName;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private UserRoleEnum role;

	private Boolean isDeleted = false;

	public User() {
	}

	private User(String email) {
		this.email = email;
	}

	private User(String nickName, String email, String password, UserRoleEnum role) {
		this.nickName = nickName;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public static User create(String nickName, String email, String password, UserRoleEnum role) {
		return new User(nickName, email, password, role);
	}
	// --------------------------  유효성 검증 --------------------------

	// 이메일 검증
	public static void generateEmail(String email) {

		if (email == null || email.isEmpty()) {
			throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "Email must not be empty");
		}

		if (!validateEmail(email)) {
			throw new BusinessException(ErrorCode.DUPLICATE_EMAIL, "Email is not valid");
		}
	}

	// 이메일 형식 검증
	public static boolean validateEmail(String email) {
		if(!emailPattern.matcher(email).matches()) {
			throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "이메일 형식이 맞는지 확인하세요.");
		}
		return true;
	}

	// 비밀번호 형식 검증
	public static void validatePassword(String password) {
		if (!passwordPattern.matcher(password).matches()) {
			throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE,
				"Password must contain at least 8 characters, including uppercase, lowercase, number, and special character.");
		}
	}

	// 비밀번호 암호화
	public static String generateEncryptedPassword(String rawPassword) {
		return BCrypUtil.encrypt(rawPassword);
	}

	public static Boolean matchesPassword(String requestPassword, String storagePassword) {
		if (!BCrypUtil.matches(requestPassword, storagePassword)) {
			throw new BusinessException(ErrorCode.LOGIN_FAILED);
		}
		return true;
	}

	public void markAsDeleted() {
		this.isDeleted = true;
	}
}
