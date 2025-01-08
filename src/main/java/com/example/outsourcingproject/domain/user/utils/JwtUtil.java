// package com.example.outsourcingproject.domain.user.utils;
//
// import javax.crypto.SecretKey;
//
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Component;
//
// import io.jsonwebtoken.security.Keys;
//
// @Component
// public class JwtUtil {
//
// 	private final SecretKey key;
// 	private static final long TOKEN_VALID_TIME = 1000L * 60 * 60;
//
// 	public JwtUtil(@Value("{secret-key}") String key) {
// 		this.key = Keys.hmacShaKeyFor(key.getBytes());
// 	}
//
//
// }
