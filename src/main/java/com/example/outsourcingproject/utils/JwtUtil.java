package com.example.outsourcingproject.utils;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private final SecretKey key;

	@Value("${jwt.expiration}")
	private long expiration;

	//1. SecretKey 를 생성자에서 초기화
	public JwtUtil(@Value("${secret-key}") String base64Key) {
		this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(base64Key));
	}

	// 2. JWT 토큰 생성
	public String generateToken(Long userId, String role) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", role);
		return createToken(claims, userId.toString());
	}

	// 3. JWT 토큰에서 사용자 id 추출
	public Long extractUserId(String token) {
		return Long.parseLong(extractClaims(token).getSubject());
	}

	// 4. JWT 토큰에서 사용자 Role 추출
	public String extractRole(String token) {
		return extractClaims(token).get("role", String.class);
	}

	// --------------------- public methods ---------------------
	public boolean validateToken(String token) {
		try {
			return !extractClaims(token).getExpiration().before(new Date());
		} catch (Exception e) {
			return false;
		}
	}

	// --------------------- private methods ---------------------
	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder()
			.setClaims(claims) // 사용자 정의 클레임 설정
			.setSubject(subject) // 사용자 ID 설정
			.setIssuedAt(new Date()) // 토큰 발행 시간
			.setExpiration(new Date(System.currentTimeMillis() + expiration)) // 토큰 만료 시간 설정
			.signWith(key, SignatureAlgorithm.HS256) // key와 서명 알고리즘 설정
			.compact(); //최종 토큰 생성
	}

	private Claims extractClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key) // 서명 검증
			.build()
			.parseClaimsJws(token) // JWT 파싱
			.getBody(); // 클레임 반환
	}

}
