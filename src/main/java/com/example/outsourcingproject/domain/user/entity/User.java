package com.example.outsourcingproject.domain.user.entity;

import com.example.outsourcingproject.domain.user.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Table(name = "user")
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Column(nullable = false)
	private String nickName;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private UserRoleEnum role;

	private Boolean isDeleted = false;
}
