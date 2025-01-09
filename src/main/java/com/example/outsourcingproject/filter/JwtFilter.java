package com.example.outsourcingproject.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.outsourcingproject.utils.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	public JwtFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		// 1. JWT 추출
		String token = request.getHeader("Authorization");

		// 2. JWT 검증 (토큰이 없으면 doFilter로 전달됩니다)
		if (token != null & jwtUtil.validateToken(token)) {

			// 2.1 사용자 정보 추출
			Long userId = jwtUtil.extractUserId(token);
			String role = jwtUtil.extractRole(token);

			// 2.2 Authentication 객체 생성 (Spring Security에서 사용되는 인증 객체)
			Authentication auth = new UsernamePasswordAuthenticationToken(
				userId,
				null, // 자격 증명. JWT 인증에서는 사용하지 않음
				List.of(new SimpleGrantedAuthority("ROLE_" + role)) // 사용자 권한 정보
			);

			// 2.3 SecurityContext에 인증 객체 저장
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
		// 3. 다음 필터로 요청 전달
		filterChain.doFilter(request, response);
	}
}


