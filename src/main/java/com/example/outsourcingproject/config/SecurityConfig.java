package com.example.outsourcingproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.outsourcingproject.filter.JwtFilter;
import com.example.outsourcingproject.exception.AuthenticationEntryPoint;
import com.example.outsourcingproject.exception.JwtAccessDeniedHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true) // @PreAuthorize 활성화
public class SecurityConfig {

	private final JwtFilter jwtFilter;

	// PasswordEncoder Bean 등록
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// AuthenticationManger Bean 등록
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws
		Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	// SecurityFilterChain 설정
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()) // CSRF 설정 비활성화
			.httpBasic(basic -> basic.disable())
			.formLogin(form -> form.disable())
			.exceptionHandling(exceptions -> exceptions
				.authenticationEntryPoint(new AuthenticationEntryPoint()) // 인증 실패 처리
				.accessDeniedHandler(new JwtAccessDeniedHandler()) // 권한 부족 처리
			)
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
				.requestMatchers("/users/login", "/users/signup").permitAll() // 로그인, 회원가입 경로는 인증 없이 접근 가능
				.requestMatchers("/api/**").hasAnyRole("CUSTOMER", "OWNER") // CUSTOMER, OWNER 둘 다 접근 가능 // 로그인, 회원가입 경로는 인증 없이 접근 가능
				.anyRequest().authenticated() // // 나머지 요청은 인증 필요
			)
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가

		return http.build(); // HttpSecurity 객체를 SecurityFilterChain으로 변환
	}
}